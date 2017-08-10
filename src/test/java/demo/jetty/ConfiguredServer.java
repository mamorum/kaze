package demo.jetty;

import kaze.App;
import kaze.server.Jetty;

public class ConfiguredServer {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    App.get("/err", (req, res) -> {
      throw new Exception("/err");
    });
    Jetty.location("/public");
    Jetty.session(60);
    Jetty.connector(60000);
    Jetty.thread(10, 10, 50000);
    Jetty.listen("0.0.0.0", 8080);
  }
}
