package kaze.test.http.junit;

import org.junit.Test;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

public class ReqArrayParamTest {
  
  @Http({"GET", "/ice"})
  public void names(Req req, Res res) {
    res.json("name", req.arrayParam("name"));
  }
  @Test
  public void names() throws Exception {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/ice?name=vanilla&name=chocolate"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"name\":[\"vanilla\",\"chocolate\"]}"
    );
  }
  
  @Http({"POST", "/ice"})
  public void ids(Req req, Res res) {
    res.json("id", req.arrayParam("id", Long[].class));
  }
  @Test
  public void ids() throws Exception {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/ice",
        "id=1&id=2&id=3"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":[1,2,3]}"
    );
  }
}
