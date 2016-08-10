package kaze.it.http.req;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.HttpRes;
import kaze.it.http.tool.ItCase;

public class ReqParamTest extends ItCase {
  
  final String uri = "/candy";
  
  @Http({Method.GET, uri})
  public void name(Req req, Res res) {
    res.json("name", req.param("name"));
  }
  @Test
  public void name() {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/candy?name=apple"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"name\":\"apple\"}"
    ).close();
  }
  
  @Http({Method.POST, uri})
  public void id(Req req, Res res) {
    res.json("id", req.param("id", Long.class));
  }
  @Test
  public void id() {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/candy",
        "id=8"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":8}"
    ).close();
  }
}
