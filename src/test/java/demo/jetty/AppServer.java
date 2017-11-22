package demo.jetty;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class AppServer {
  public static void main(String[] args) {
    Gson gson = new Gson();
    App app = new App();
    app.parser(gson::fromJson, gson::toJson);
    app.get("/", (req, res) -> {
      res.json("msg", "Hello");
    });
    app.get("/id/:id", (req, res) -> {
      res.json("id", req.path(":id"));
    });
    app.get("/err", (req, res) -> {
      throw new Exception("Exception");
    });
    app.get("/err/run", (req, res) -> {
      throw new RuntimeException("Runtime Exception");
    });
    Jetty jetty = new Jetty();
    jetty.app(app, "/*");
    jetty.listen(8080);
  }
}
