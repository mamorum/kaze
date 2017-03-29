package it.http.res;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.ItCase;
import kaze.http.Req;
import kaze.http.Res;

public class ResJsonTest extends ItCase {

  @Before public void regist() {
    http.get("/res/json/str", ResJsonTest::jsonStr);
    http.get("/res/json/hi", ResJsonTest::hi);
    http.get("/res/json/error", ResJsonTest::error);
  }

  // TODO test Res.json(String)
  public static void jsonStr(Req req, Res res) {
    res.json("{\"msg\":\"str\"}");
  }
  @Test
  public void jsonStr() {
    HttpReq.get(
        "http://localhost:8080/res/json/str"
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"msg\":\"str\"}"
    ).close();
  }

  public static void hi(Req req, Res res) {
    res.json("msg", "Hi!");
  }
  @Test
  public void hi() {
    HttpReq.get(
        "http://localhost:8080/res/json/hi"
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"msg\":\"Hi!\"}"
    ).close();
  }

  
  public static void error(Req req, Res res) {
    res.status(400).json(
        "msg", "name is required.",
        "cause", "validation"
    );
  }
  @Test
  public void error() {
    HttpReq.get(
        "http://localhost:8080/res/json/error"
    ).statusIs(400).typeIsJsonUtf8().bodyIs(
        "{\"msg\":\"name is required.\"," +
          "\"cause\":\"validation\"}"
    ).close();
  }
}
