package demo;

import kaze.App;
import kaze.server.Jetty;

public class AppServer {
  public static void main(String[] args) {
    App app = new App();
    app.get("/json", (req, res) -> {
      res.json("msg", "Hello");
    });
    Jetty jetty = new Jetty(app);
    jetty.listen(8080);
  }
}
