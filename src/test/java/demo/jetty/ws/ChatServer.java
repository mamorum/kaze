package demo.jetty.ws;

import kaze.server.Jetty;
import kaze.server.Jws;

public class ChatServer {
  public static void main(String[] args) {
    Jws.on();
    Jws.add(ChatSocket.class);
    Jetty.location("/public");
    Jetty.listen(8080);
  }
}
