package demo;

import javax.servlet.http.HttpSession;

import kaze.App;
import kaze.server.Jetty;

public class Main {
  public static void main(String[] args) {
    App app = new App();
    app.get("/hello", (req, res) -> {
      res.json("msg", "Hello.");
    });
    app.get("/hi", (req, res) -> {
      res.json("msg", "Hi!");
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
    Jetty jetty = new Jetty();
    jetty.location("/public");
    jetty.listen(8080);
  }
}
