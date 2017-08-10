package demo.tomcat;

import java.io.File;

import kaze.server.ETomcat;

public class FileServer {
  public static void main(String[] args) {
//    App.get("/", (req, res) -> {
//      res.html("<p>Hello World</p>");
//    });
//    App.get("/err", (req, res) -> {
//      throw new Exception("/err");
//    });
    ETomcat.location(new File("src/test/resources/public"));
//    ETomcat.session(60);
//    ETomcat.connector(60000);
//    ETomcat.thread(10, 10, 50000);
    ETomcat.listen("0.0.0.0", 8080);
  }
}
