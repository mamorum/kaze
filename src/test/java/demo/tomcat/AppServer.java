package demo.tomcat;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.ETomcat;

public class AppServer {
  public static void main(String[] args) {
    Gson gson = new Gson();
    App.parser(gson::fromJson, gson::toJson);
    App.get("/", (req, res) -> {
      res.json("msg", "Hello");
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
    App.get("/err", (req, res) -> {
      throw new Exception("Exception");
    });
    App.get("/err/run", (req, res) -> {
      throw new RuntimeException("Runtime Exception");
    });
    ETomcat.listen(8080);
  }
}
