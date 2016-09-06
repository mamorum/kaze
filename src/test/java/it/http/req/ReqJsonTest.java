package it.http.req;

import org.junit.Test;

import it.http.model.Address;
import it.http.model.Person;
import it.http.tool.HttpReq;
import it.http.tool.HttpRes;
import it.http.tool.ItCase;
import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;

public class ReqJsonTest extends ItCase {
  
  @Http({Method.POST, "/person/json"})
  public void pj(Req req, Res res) {
    Person p = req.json(Person.class).get();
    res.json(p);
  }
  @Test
  public void pj() {
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
  
  @Http({Method.POST, "/address/json"})
  public void aj(Req req, Res res) {
    Address p = req.json(Address.class).valid();
    res.json(p);
  }
  @Test  //OK
  public void aj() {
    String adrs = 
      "{\"zip\":\"1234567\",\"pref\":\"東京\"}";
    
    HttpReq.postJson(
        "http://localhost:8080/address/json",
        adrs
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        adrs
    ).close();
  }
  @Test  //ValidateError
  public void badAj() {
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
