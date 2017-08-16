package demo.jetty;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import demo.jetty.servlet.LogFilter;
import demo.jetty.servlet.HelloServlet;
import demo.jetty.ws.ChatSocket;
import kaze.App;
import kaze.server.Jetty;
import kaze.server.Jws;

public class ConfiguredServer {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    App.get("/err", (req, res) -> {
      throw new Exception("/err");
    });
    // Add servlet & fileter
    ////-> OK: servlet
    Jetty.handler().addServlet(HelloServlet.class, "/hello");
    ////-> OK: filter
    Jetty.handler().addFilter(LogFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

    Jws.on();
    Jws.add(ChatSocket.class);
    //-> NG: websocket (because of not setting server)
//    ServerContainer ws =
//        WebSocketServerContainerInitializer.configureContext(Jetty.handler());
//    ws.addEndpoint(ChatSocket.class);

    Jetty.location("/public");
    Jetty.session(60);
    Jetty.connector(60000);
    Jetty.thread(10, 10, 50000);
    Jetty.listen("0.0.0.0", 8080);
  }
}
