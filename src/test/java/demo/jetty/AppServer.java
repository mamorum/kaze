package demo.jetty;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class AppServer {
  public static void main(String[] args) {
    App app = new App();
    Gson gson = new Gson();
    app.json.parser(gson::fromJson, gson::toJson);
    app.get.add("/", (req, res) -> {
      res.json("msg", "Hello");
    });
    app.get.add("/id/:id", (req, res) -> {
      res.json("id", req.path(":id"));
    });
    app.get.add("/err", (req, res) -> {
      throw new Exception("Exception");
    });
    app.get.add("/err/run", (req, res) -> {
      throw new RuntimeException("Runtime Exception");
    });
    Jetty.app(app, "/*");
    Jetty.listen(8080);
  }
}
