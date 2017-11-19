package demo.jetty;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

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
    App.get("/err", (req, res) -> {
      throw new Exception("Exception");
    });
    App.get("/err/run", (req, res) -> {
      throw new RuntimeException("Runtime Exception");
    });
    Jetty.context().addServlet(App.Servlet.class, "/app/*");
    Jetty.listen(8080);
  }
}
