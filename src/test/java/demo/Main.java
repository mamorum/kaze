package demo;

import kaze.App;
import kaze.server.Jetty;

public class Main {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.send("Hello, World.");
    });
    Jetty.listen(8080);
  }
}
