package demo;

import kaze.App;

public class Main {
  public static void main(String[] args) {
    App app = new App();
    app.get("/hello", (req, res) -> {
      res.json("msg", "Hello.");
    });
    app.get("/hi", (req, res) -> {
      res.json("msg", "Hi!");
    });
    app.listen(8080);
  }
}
