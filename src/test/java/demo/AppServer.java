package demo;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class AppServer {
  public static void main(String[] args) {
    Gson gson = new Gson();
    App.parser(gson::fromJson, gson::toJson);
    App.get("/json", (req, res) -> {
      res.json("msg", "Hello");
    });
    Jetty.listen(8080);
  }
}
