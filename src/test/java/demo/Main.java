package demo;

import kaze.Http;

public class Main {
  public static void main(String[] args) {
    Http.server().get("/hello", (req, res) -> {
      res.json("msg", "Hello World!");
    }).listen();
  }
}
