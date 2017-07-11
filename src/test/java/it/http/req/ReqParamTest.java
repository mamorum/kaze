package it.http.req;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.HttpRes;
import it.http.tool.ItCase;
import kaze.Req;
import kaze.Res;

public class ReqParamTest extends ItCase {

  @Before public void regist() {
    http.get("/candy", ReqParamTest::name);
    http.post("/candy", ReqParamTest::id);
  }

  // Test target
  public static void name(Req req, Res res) {
    res.json("name", req.param("name"));
  }
  @Test public void name() {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/candy?name=apple"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"name\":\"apple\"}"
    ).close();
  }
  
  // Test target
  public static void id(Req req, Res res) {
    res.json("id", req.param("id", Long.class));
  }
  @Test public void id() {
    HttpRes res = HttpReq.postParams(
        "http://localhost:8080/candy",
        "id=8"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":8}"
    ).close();
  }
}
