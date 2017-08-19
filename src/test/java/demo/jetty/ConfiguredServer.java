package demo.jetty;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import demo.jetty.servlet.LogFilter;
import demo.jetty.servlet.SessionListener;
import demo.jetty.servlet.ContextListener;
import demo.jetty.servlet.HelloServlet;
import demo.jetty.ws.ChatSocket;
import kaze.App;
import kaze.server.Jetty;
import kaze.server.Jws;

public class ConfiguredServer {
  public static void main(String[] args) {
    Gson gson = new Gson();
    App.parser(gson::fromJson, gson::toJson);
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    App.get("/err", (req, res) -> {
      throw new Exception("/err");
    });
    App.get("/ssn/set", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      ss.setAttribute("ssn", "val");
      res.json("msg", "set");
    });
    App.get("/ssn/del", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      ss.invalidate();
      res.json("msg", "del");
    });
    App.get("/ssn/read", (req, res) -> {
      HttpSession ss = req.srv.getSession(false);
      res.json("msg", ss.getAttribute("ssn"));
    });

    Jetty.handler().addServlet(HelloServlet.class, "/hello");
    Jetty.handler().addFilter(LogFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    Jetty.handler().addEventListener(new ContextListener());
    Jetty.handler().getSessionHandler().addEventListener(new SessionListener());
    //<- session handler じゃないとダメ

    Jws.on();
    Jws.add(ChatSocket.class);
    //-> NG: websocket (because of not setting server)
//    ServerContainer ws =
//        WebSocketServerContainerInitializer.configureContext(Jetty.handler());
//    ws.addEndpoint(ChatSocket.class);

    Jetty.location("/public");
    Jetty.session(30); // seconds 秒（session listener より後で大丈夫）
    Jetty.connector(60000);
    Jetty.thread(10, 10, 50000);
    Jetty.listen("0.0.0.0", 8080);
  }
}
