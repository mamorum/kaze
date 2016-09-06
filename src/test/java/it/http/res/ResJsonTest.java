package it.http.res;

import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.ItCase;
import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;

public class ResJsonTest extends ItCase {

  // TODO test Res.json(String)
  @Http({Method.GET, "/res/json/str"})
  public void jsonStr(Req req, Res res) {
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
  
  @Http({Method.GET, "/res/json/hi"})
  public void hi(Req req, Res res) {
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
  
  @Http({Method.GET, "/res/json/error"})
  public void error(Req req, Res res) {
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
