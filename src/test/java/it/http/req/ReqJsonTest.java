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

public class ReqJsonTest extends ItCase {

  @Before public void regist() {
    http.post("/person/json", ReqJsonTest::pj);
    http.post("/address/json", ReqJsonTest::aj);
  }

  // Test target:
  public static void pj(Req req, Res res) {
    Person p = req.json(Person.class);
    res.json(p);
  }
  @Test public void pj() {
    String json =
      "{\"id\":12345,\"name\":\"Bob\",\"langs\":[\"C\",\"Java\",\"JS\"]}";
    HttpRes res = HttpReq.postJson(
        "http://localhost:8080/person/json",
        json
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        json
    ).close();
  }

  // Test target:
  public static void aj(Req req, Res res) {
    Address p = req.json(Address.class);
    res.json(p);
  }
  @Test public void aj() { // -> OK
    String adrs =
      "{\"zip\":\"1234567\",\"pref\":\"東京\"}";
    HttpReq.postJson(
        "http://localhost:8080/address/json",
        adrs
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        adrs
    ).close();
  }
  @Test public void badAj() {  // -> ValidateError
    String adrs =
      "{\"zip\":\"12345\",\"pref\":\"\"}";
    HttpReq.postJson(
        "http://localhost:8080/address/json",
        adrs
    ).statusIs(400).typeIsJsonUtf8().bodyContains(
        "\"cause\":\"validate\"",
        "\"fields\":[{",
        "\"name\":\"pref\",\"cause\":\"NotEmpty\",\"msg\":",
        "\"name\":\"zip\",\"cause\":\"Size\",\"msg\":"
    ).close();
  }
}
