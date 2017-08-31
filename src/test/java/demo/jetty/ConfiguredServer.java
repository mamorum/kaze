package demo.jetty;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.google.gson.Gson;

import demo.jetty.servlet.ContextListener;
import demo.jetty.servlet.HelloServlet;
import demo.jetty.servlet.HelloLogFilter;
import demo.jetty.servlet.RequestListener;
import demo.jetty.servlet.SessionListener;
import demo.jetty.ws.ChatSocket;
import kaze.App;
import kaze.server.Jetty;

public class ConfiguredServer {
  public static void main(String[] args) throws Exception {
    Gson gson = new Gson();
    App.parser(gson::fromJson, gson::toJson);
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    App.get("/err", (req, res) -> {
      throw new Exception("/err");
    });
    App.get("/ssn", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      if (ss.isNew()) res.json("isNew", true);
      else res.json("isNew", false);;
    });
    Jetty jty = new Jetty(10, 10, 50000);
    jty.http(60000, 30);
    jty.location("/public");
    addServletComponent(jty);
    addWebsocketComponent(jty);
    jty.listen("0.0.0.0", 8080);
  }
  private static void addServletComponent(Jetty jty) {
    jty.handler().addServlet(HelloServlet.class, "/hello");
    jty.handler().addFilter(HelloLogFilter.class, "/hello", EnumSet.of(DispatcherType.REQUEST));
    jty.handler().addEventListener(new RequestListener());
    jty.handler().addEventListener(new ContextListener());
    jty.handler().getSessionHandler().addEventListener(new SessionListener());
    //<- HttpSessionListener needs to be added to SessionHandler.
  }
  private static void addWebsocketComponent(Jetty jty)
    throws ServletException, DeploymentException
  {
    ServerContainer ws =
      WebSocketServerContainerInitializer.configureContext(jty.handler());
    ws.addEndpoint(ChatSocket.class);
  }
}