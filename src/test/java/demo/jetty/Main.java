package demo.jetty;

import kaze.App;
import kaze.server.EJetty;

public class Main {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    EJetty.listen(8080);
  }
}
