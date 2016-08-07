package kaze.it.http.res;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResWriteTest extends ItCase {

  @Http({Method.GET, "/res/write/hi"})
  public void hi(Req req, Res res) {
    res.write("Hi!");
  }
  @Test
  public void hi() throws Exception {
    HttpReq.get(
        "http://localhost:8080/res/write/hi"
    ).statusIs(200).typeIs(
        "text/plain; charset=UTF-8"
    ).bodyIs("Hi!");
  }
  
  @Http({Method.GET, "/res/write/error"})
  public void error(Req req, Res res) {
    res.status(400).write("Error.");
  }
  @Test
  public void error() throws Exception {
    HttpReq.get(
        "http://localhost:8080/res/write/error"
    ).statusIs(400).typeIs(
        "text/plain; charset=UTF-8"
    ).bodyIs("Error.");
  }
}
