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

public class FullServer {
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
    initServletComponent();
    initWebsocketComponent();
    Jetty.thread(20, 20, 50000);
    Jetty.http(60000, 300); // session timeout: 5min
    Jetty.location("/public");
    Jetty.listen("0.0.0.0", 8080);
  }
  private static void initServletComponent() {
    Jetty.handler().addServlet(HelloServlet.class, "/hello");
    Jetty.handler().addFilter(HelloLogFilter.class, "/hello", EnumSet.of(DispatcherType.REQUEST));
    Jetty.handler().addEventListener(new RequestListener());
    Jetty.handler().addEventListener(new ContextListener());
    Jetty.handler().getSessionHandler().addEventListener(new SessionListener());
    //<- HttpSessionListener needs to be added to SessionHandler.
  }
  private static void initWebsocketComponent()
    throws ServletException, DeploymentException
  {
    ServerContainer ws =
      WebSocketServerContainerInitializer.configureContext(Jetty.handler());
    ws.addEndpoint(ChatSocket.class);
  }
}