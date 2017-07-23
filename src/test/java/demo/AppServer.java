package demo;

import kaze.App;
import kaze.server.Jetty;

public class AppServer {
  public static void main(String[] args) {
    App app = new App();
    app.get("/", (req, res) -> {
      res.json("msg", "Welcome.");
    });
    Jetty jetty = new Jetty(app);
    jetty.listen(8080);
  }
}
