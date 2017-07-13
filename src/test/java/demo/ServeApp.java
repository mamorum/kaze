package demo;

import kaze.App;
import kaze.server.Jetty;

public class ServeApp {
  public static void main(String[] args) {
    App app = new App();
    app.get("/hi", (req, res) -> {
      res.json("msg", "Hi!");
    });
    Jetty jetty = new Jetty(app);
    jetty.listen(8080);
  }
}
