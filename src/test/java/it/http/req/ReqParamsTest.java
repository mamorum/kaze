package it.http.req;

import org.junit.Before;
import org.junit.Test;

import it.http.model.Address;
import it.http.model.Person;
import it.http.tool.HttpReq;
import it.http.tool.HttpRes;
import it.http.tool.ItCase;
import kaze.Req;
import kaze.Res;

public class ReqParamsTest extends ItCase {

  @Before public void regist() {
    http.post("/person/params", ReqParamsTest::pp);
    http.post("/address/params", ReqParamsTest::ap);
  }

  // Test target
  public static void pp(Req req, Res res) {
    Person p = req.params(Person.class);
    res.json(p);
  }
  @Test public void pp() {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/person/params",
        "id=1234&name=Tom&langs=Perl&langs=PHP"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":1234,\"name\":\"Tom\",\"langs\":[\"Perl\",\"PHP\"]}"
    ).close();
  }

  // Test target
  public static void ap(Req req, Res res) {
    Address p = req.params(Address.class);
    res.json(p);
  }
  @Test public void ap() {  // -> OK
    HttpReq.postParams(
        "http://localhost:8080/address/params",
        "zip=1234567&pref=東京"
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"zip\":\"1234567\",\"pref\":\"東京\"}"
    ).close();
  }
  @Test public void badAp() {  // -> ValidateError
    HttpReq.postParams(
        "http://localhost:8080/address/params",
        "zip=12345&pref="
    ).statusIs(400).typeIsJsonUtf8().bodyContains(
        "\"cause\":\"validate\"",
        "\"fields\":[{",
        "\"name\":\"pref\",\"cause\":\"NotEmpty\",\"msg\":",
        "\"name\":\"zip\",\"cause\":\"Size\",\"msg\":"
    ).close();
  }
}
