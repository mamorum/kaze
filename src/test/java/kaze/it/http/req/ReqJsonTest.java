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

public class ReqJsonTest extends ItCase {
  
  @Http({Method.POST, "/person/json"})
  public void pj(Req req, Res res) {
    Person p = req.json(Person.class).get();
    res.json(p);
  }
  @Test
  public void pj() throws Exception {
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
  
  @Http({Method.POST, "/address/json"})
  public void aj(Req req, Res res) {
    Address p = req.json(Address.class).valid();
    res.json(p);
  }
  @Test  //OK
  public void aj() throws Exception {
    String adrs = 
      "{\"zip\":\"1234567\",\"pref\":\"東京\"}";
    
    HttpReq.postJson(
        "http://localhost:8080/address/json",
        adrs
    ).statusIs(200).contentTypeIsJson().bodyIs(
        adrs
    );
  }
  @Test  //ValidateError
  public void badAj() throws Exception {
    String adrs = 
      "{\"zip\":\"12345\",\"pref\":\"\"}";
    
    HttpReq.postJson(
        "http://localhost:8080/address/json",
        adrs
    ).statusIs(400).contentTypeIsJson().bodyContains(
        "\"cause\":\"validate\"",
        "\"fields\":[{",
        "\"name\":\"pref\",\"cause\":\"NotEmpty\",\"msg\":",
        "\"name\":\"zip\",\"cause\":\"Size\",\"msg\":"
    );
  }
}
