package it.http.res;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.ItCase;
import kaze.http.Req;
import kaze.http.Res;

public class ResWriteTest extends ItCase {

  @Before public void regist() {
    http.get("/res/write/hi", ResWriteTest::hi);
    http.get("/res/write/error", ResWriteTest::error);
  }

  public static void hi(Req req, Res res) {
    res.write("Hi!");
  }
  @Test
  public void hi() {
    HttpReq.get(
        "http://localhost:8080/res/write/hi"
    ).statusIs(200).typeIs(
        "text/plain; charset=UTF-8"
    ).bodyIs("Hi!").close();
  }
  
  public static void error(Req req, Res res) {
    res.status(400).write("Error.");
  }
  @Test
  public void error() {
    HttpReq.get(
        "http://localhost:8080/res/write/error"
    ).statusIs(400).typeIs(
        "text/plain; charset=UTF-8"
    ).bodyIs("Error.").close();
  }
}
