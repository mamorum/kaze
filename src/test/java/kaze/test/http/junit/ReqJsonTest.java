package kaze.test.http.junit;

import org.junit.Test;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.model.Person;
import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

public class ReqJsonTest {
  
  @Http({"POST", "/person/json"})
  public void json(Req req, Res res) {
    Person p = req.json(Person.class).get();
    res.json(p);
  }
  @Test
  public void json() throws Exception {
    String json = 
      "{\"id\":12345,\"name\":\"Bob\",\"langs\":[\"C\",\"Java\",\"JS\"]}";
    
    HttpRes res = HttpReq.postJson(
        "http://localhost:8080/person/json",
        json
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        json
    );
  }
}
