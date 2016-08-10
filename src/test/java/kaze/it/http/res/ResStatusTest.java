package kaze.it.http.res;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResStatusTest extends ItCase {
  
  @Http({Method.GET, "/res/status/:code"})
  public void status(Req req, Res res) {
    res.status(
      req.uri(":code", Integer.class)
    );
  }
  @Test
  public void s200() {
    HttpReq.get(
        "http://localhost:8080/res/status/200"
    ).statusIs(200).typeIs(null).bodyIsEmpty().close();
  }
  @Test
  public void s500() {
    HttpReq.get(
        "http://localhost:8080/res/status/500"
    ).statusIs(500).typeIs(null).bodyIsEmpty().close();
  }
}
