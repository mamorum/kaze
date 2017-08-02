package demo.jetty;

import kaze.server.Jetty;

public class ConfiguredServer {
  public static void main(String[] args) {
    Jetty.thread(10, 10, 50000);
    Jetty.http(60000);
    Jetty.session(60);
    Jetty.location("/public");
    Jetty.listen("0.0.0.0", 8080);
  }
}
