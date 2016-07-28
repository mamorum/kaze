package kaze.test.http.junit;

import org.junit.Test;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.model.Address;
import kaze.test.http.model.Person;
import kaze.test.http.tool.HttpReq;
import kaze.test.http.tool.HttpRes;

public class ReqParamsTest {

  @Http({"POST", "/person/params"})
  public void pp(Req req, Res res) {
    Person p = req.params(Person.class).get();
    res.json(p);
  }
  @Test
  public void pp() throws Exception {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/person/params",
        "id=1234&name=Tom&langs=Perl&langs=PHP"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":1234,\"name\":\"Tom\",\"langs\":[\"Perl\",\"PHP\"]}"
    );
  }
  
  
  @Http({"POST", "/address/params"})
  public void ap(Req req, Res res) {
    Address p = req.params(Address.class).valid();
    res.json(p);
  }
  @Test  //OK
  public void ap() throws Exception {
    HttpReq.postParams(
        "http://localhost:8080/address/params",
        "zip=1234567&pref=Tokyo"
    ).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"zip\":\"1234567\",\"pref\":\"Tokyo\"}"
    );
  }
  @Test  //ValidateError
  public void badAp() throws Exception {
    HttpReq.postParams(
        "http://localhost:8080/address/params",
        "zip=12345&pref="
    ).statusIs(400).contentTypeIsJson().bodyContains(
        "\"cause\":\"validate\"",
        "\"fields\":[{",
        "\"name\":\"pref\",\"cause\":\"NotEmpty\",\"msg\":",
        "\"name\":\"zip\",\"cause\":\"Size\",\"msg\":"
    );
  }
}
