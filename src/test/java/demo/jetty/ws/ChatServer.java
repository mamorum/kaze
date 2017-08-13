package demo.jetty.ws;

import kaze.server.Jetty;
import kaze.server.Jws;

public class ChatServer {
  public static void main(String[] args) {
    Jws.add(ChatSocket.class);
    Jws.install();
    Jetty.location("/public");
    Jetty.listen(8080);
  }
}
