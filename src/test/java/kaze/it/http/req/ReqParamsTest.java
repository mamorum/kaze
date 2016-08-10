package kaze.it.http.req;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.model.Address;
import kaze.it.http.model.Person;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.HttpRes;
import kaze.it.http.tool.ItCase;

public class ReqParamsTest extends ItCase {

  @Http({Method.POST, "/person/params"})
  public void pp(Req req, Res res) {
    Person p = req.params(Person.class).get();
    res.json(p);
  }
  @Test
  public void pp() {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/person/params",
        "id=1234&name=Tom&langs=Perl&langs=PHP"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":1234,\"name\":\"Tom\",\"langs\":[\"Perl\",\"PHP\"]}"
    ).close();
  }
  
  
  @Http({Method.POST, "/address/params"})
  public void ap(Req req, Res res) {
    Address p = req.params(Address.class).valid();
    res.json(p);
  }
  @Test  //OK
  public void ap() {
    HttpReq.postParams(
        "http://localhost:8080/address/params",
        "zip=1234567&pref=東京"
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"zip\":\"1234567\",\"pref\":\"東京\"}"
    ).close();
  }
  @Test  //ValidateError
  public void badAp() {
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
