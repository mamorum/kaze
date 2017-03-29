package it.http.req;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.HttpRes;
import it.http.tool.ItCase;
import kaze.http.Req;
import kaze.http.Res;

public class ReqListParamTest extends ItCase {

  @Before public void regist() {
    http.get("/ice", ReqListParamTest::names);
    http.post("/ice", ReqListParamTest::ids);
  }

  final String uri = "/ice";

  public static void names(Req req, Res res) {
    res.json("names", req.listParam("names"));
  }
  @Test public void names() {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/ice?names=vanilla&names=chocolate"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"names\":[\"vanilla\",\"chocolate\"]}"
    ).close();
  }

  public static void ids(Req req, Res res) {
    res.json("id", req.listParam("id", Long.class));
  }
  @Test public void ids() {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/ice",
        "id=1&id=2&id=3"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":[1,2,3]}"
    ).close();
  }
}
