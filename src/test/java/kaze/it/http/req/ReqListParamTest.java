package kaze.it.http.req;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.HttpRes;
import kaze.it.http.tool.ItCase;

public class ReqListParamTest extends ItCase {
  
  final String uri = "/ice";
  
  @Http({Method.GET, uri})
  public void names(Req req, Res res) {
    res.json("names", req.listParam("names"));
  }
  @Test
  public void names() {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/ice?names=vanilla&names=chocolate"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"names\":[\"vanilla\",\"chocolate\"]}"
    ).close();
  }
  
  @Http({Method.POST, uri})
  public void ids(Req req, Res res) {
    res.json("id", req.listParam("id", Long.class));
  }
  @Test
  public void ids() {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/ice",
        "id=1&id=2&id=3"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":[1,2,3]}"
    ).close();
  }
}
