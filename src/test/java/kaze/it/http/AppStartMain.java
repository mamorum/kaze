package kaze.it.http;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class AppStartMain {

  @Http({"GET", "/hello"})
  public void hello(Req req, Res res) {
    res.json("msg", "Hello!");
  }

  public static void main(String[] args) {
    App.start("kaze.it.http");
  }
}
