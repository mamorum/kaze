package demo;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class FullConfServer {
  private static final Gson gson = new Gson();
  public static void main(String[] args) {
    App.parser(gson::fromJson, gson::toJson);
    App.get("/json", (req, res) -> {
      res.json("msg", "Hello");
    });
    App.get("/err", (req, res) -> {
      throw new Exception("Exception");
    });
    App.get("/err/run", (req, res) -> {
      throw new RuntimeException("Runtime Exception");
    });
    App.get("/id/:id", (req, res) -> {
      res.json("id", req.path(":id"));
    });
    App.get("/session/set", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      ss.setAttribute("session", "value");
      res.json("msg", "set");
    });
    App.get("/session/read", (req, res) -> {
      HttpSession ss = req.srv.getSession(false);
      res.json("msg", ss.getAttribute("session"));
    });
    Jetty.thread(10, 10, 50000);
    Jetty.http(60000);
    Jetty.session(60);
    Jetty.location("/public");
    Jetty.listen("0.0.0.0", 8080);
  }
}
