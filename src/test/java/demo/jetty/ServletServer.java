package demo.jetty;

import kaze.App;
import kaze.server.ServletJetty;

public class ServletServer {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    ServletJetty.location("/public");
    ServletJetty.session(60);
    ServletJetty.connector(60000);
    ServletJetty.thread(10, 10, 50000);
    ServletJetty.listen("0.0.0.0", 8080);
  }
}
