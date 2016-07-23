package kaze.test.http;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Main {

  @Http({"GET", "/"})
  public void index(Req req, Res res) {
    res.json("msg", "Hello!");
  }

  public static void main(String[] args) {
    App.start("kaze.test.http");
  }
}
