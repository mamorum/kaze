package kaze.test.http;

import org.junit.Test;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Serve4Test {

  @Http({"GET", "/"})
  public void index(Req req, Res res) {
    res.json("msg", "Hello!");
  }

  @Test public void serve() {
    App.start("kaze.test.http");
  }
}
