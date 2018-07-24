package demo.jetty;

import kaze.App;
import kaze.opt.Jetty;

public class ExitServer {
  public static void main(String[] args) {
    App app = new App();
    app.get("/exit", (req, res) -> {
      res.html("<p>System Exit.</p>");
      Jetty.server.setStopAtShutdown(true);
      (new Thread() {
        public void run() { System.exit(0); }
      }).start();
    });
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    Jetty.listen(8080);
  }
}