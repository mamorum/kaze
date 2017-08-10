package demo.tomcat;

import kaze.App;
import kaze.server.ETomcat;

public class ConfiguredServer {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    App.get("/err", (req, res) -> {
      throw new Exception("/err");
    });
    ETomcat.location("/public");
//    ETomcat.session(60);
//    ETomcat.connector(60000);
//    ETomcat.thread(10, 10, 50000);
    ETomcat.listen("0.0.0.0", 8080);
  }
}
