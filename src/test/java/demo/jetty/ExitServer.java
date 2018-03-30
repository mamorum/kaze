package demo.jetty;

import kaze.App;
import kaze.server.Jetty;

public class ExitServer {
  public static void main(String[] args) {
    App app = new App();
    app.get("/exit", (req, res) -> {
      res.html("<p>System Exit.</p>");
      Jetty.exit(0);
    });
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    Jetty.listen(8080);
  }
}