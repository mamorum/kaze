package kaze.test.http.junit;

import org.junit.Test;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

public class ReqParamTest {
  
  @Http({"GET", "/candy"})
  public void name(Req req, Res res) {
    res.json("name", req.param("name"));
  }
  @Test
  public void name() throws Exception {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/candy?name=apple"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"name\":\"apple\"}"
    );
  }
  
  @Http({"POST", "/candy"})
  public void id(Req req, Res res) {
    res.json("id", req.param("id", Long.class));
  }
  @Test
  public void id() throws Exception {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/candy",
        "id=8"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8}"
    );
  }
}