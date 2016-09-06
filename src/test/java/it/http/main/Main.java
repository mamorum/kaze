package it.http.main;

import static kaze.Http.Method.*;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Main {

  @Http({GET, "/hello"})
  public void hello(Req req, Res res) {
    res.json("msg", "Hello World!");
  }

  public static void main(String[] args) {
    App.start("it.http");
  }
}
