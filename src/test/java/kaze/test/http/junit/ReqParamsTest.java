package kaze.test.http.junit;

import org.junit.Test;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.model.Person;
import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

public class ReqParamsTest {

  @Http({"POST", "/person/params"})
  public void params(Req req, Res res) {
    Person p = req.params(Person.class).get();
    res.json(p);
  }
  @Test
  public void params() throws Exception {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/person/params",
        "id=1234&name=Tom&langs=Perl&langs=PHP"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":1234,\"name\":\"Tom\",\"langs\":[\"Perl\",\"PHP\"]}"
    );
  }
}
