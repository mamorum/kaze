package demo;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class AppServer {
  public static void main(String[] args) {
    App app = new App();
    Gson gson = new Gson();
    App.fromJson = gson::fromJson;
    App.toJson = gson::toJson;
    app.get("/json", (req, res) -> {
      res.json("msg", "Hello");
    });
    Jetty jetty = new Jetty(app);
    jetty.listen(8080);
  }
}
