package demo;

import javax.servlet.http.HttpSession;

import kaze.App;
import kaze.server.Jetty;

public class FullConfServer {
  public static void main(String[] args) {
    App app = new App();
    app.get("/json", (req, res) -> {
      res.json("msg", "Hello");
    });
    app.get("/err", (req, res) -> {
      throw new Exception("Exception");
    });
    app.get("/err/run", (req, res) -> {
      throw new RuntimeException("Runtime Exception");
    });
    app.get("/id/:id", (req, res) -> {
      res.json("id", req.path(":id"));
    });
    app.get("/session/set", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      ss.setAttribute("session", "value");
      res.json("msg", "set");
    });
    app.get("/session/read", (req, res) -> {
      HttpSession ss = req.srv.getSession(false);
      res.json("msg", ss.getAttribute("session"));
    });
    Jetty jetty = new Jetty(app);
    jetty.thread(10, 10, 50000);
    jetty.http(60000);
    jetty.session(30);
    jetty.location("/public");
    jetty.listen(8080);
  }
}
