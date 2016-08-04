package kaze.it.http.res;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResJsonTest extends ItCase {

  @Http({Method.GET, "/hi"})
  public void hi(Req req, Res res) {
    res.json("msg", "Hi!");
  }
  @Test
  public void hi() throws Exception {
    HttpReq.get(
        "http://localhost:8080/hi"
    ).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"msg\":\"Hi!\"}"
    );
  }
}
