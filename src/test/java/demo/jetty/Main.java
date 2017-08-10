package demo.jetty;

import kaze.App;
import kaze.server.Jetty;

public class Main {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    Jetty.listen(8080);
  }
}
