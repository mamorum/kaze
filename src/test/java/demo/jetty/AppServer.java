package demo.jetty;

import com.google.gson.Gson;

import kaze.App;
import kaze.opt.Jetty;

public class AppServer {
  public static void main(String[] args) {
    App app = new App();
    Gson gson = new Gson();
    app.conv(gson::fromJson, gson::toJson);
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
    Jetty.app(app, "/*");
    Jetty.listen(8080);
  }
}
