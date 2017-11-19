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
    //-> thread pool settings
    Jetty.thread().setMaxThreads(20);
    Jetty.thread().setMinThreads(20);
    Jetty.thread().setIdleTimeout(500000);
    Jetty.connector().setIdleTimeout(60000);
    Jetty.context().setContextPath("/");
    Jetty.session().setMaxInactiveInterval(
      300 // session timeout sec (300sec = 5min)
    );
    Jetty.location("/public");
    Jetty.listen("0.0.0.0", 8080);
  }
  private static void initServletComponent() {
    Jetty.context().addServlet(HelloServlet.class, "/hello");
    Jetty.context().addFilter(HelloLogFilter.class, "/hello", EnumSet.of(DispatcherType.REQUEST));
    Jetty.context().addEventListener(new RequestListener());
    Jetty.context().addEventListener(new ContextListener());
    Jetty.context().getSessionHandler().addEventListener(new SessionListener());
    //<- HttpSessionListener needs to be added to SessionHandler.
  }
  private static void initWebsocketComponent()
    throws ServletException, DeploymentException
  {
    ServerContainer ws =
      WebSocketServerContainerInitializer.configureContext(Jetty.context());
    ws.addEndpoint(ChatSocket.class);
  }
}