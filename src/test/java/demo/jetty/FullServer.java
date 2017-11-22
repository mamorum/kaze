package demo.jetty;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.servlet.ServletHolder;
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
    App app = new App();
    app.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    app.get("/err", (req, res) -> {
      throw new Exception("/err");
    });
    app.get("/ssn", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      if (ss.isNew()) res.json("isNew", true);
      else res.json("isNew", false);;
    });
    Gson gson = new Gson();
    app.parser(gson::fromJson, gson::toJson);
    //-> Jetty settings
    Jetty jetty = new Jetty();
    addServletComponent(jetty);
    addWebsocketComponent(jetty);
    ////-> thread pool settings
    jetty.thread().setMaxThreads(20);
    jetty.thread().setMinThreads(20);
    jetty.thread().setIdleTimeout(500000);
    jetty.connector().setIdleTimeout(60000);
    jetty.session().setMaxInactiveInterval(
      300 // session timeout sec (300sec=5min)
    );
    jetty.context().setContextPath("/");
    jetty.app(app, "/app/*");
    jetty.doc("/public", "/");
    jetty.listen("0.0.0.0", 8080);
  }
  private static void addServletComponent(Jetty jetty) {
    ServletHolder sh = new ServletHolder(new HelloServlet());
    sh.setAsyncSupported(true);
    // TODO set async servlet.
    jetty.context().addServlet(HelloServlet.class, "/hello");
    jetty.context().addFilter(HelloLogFilter.class, "/hello", EnumSet.of(DispatcherType.REQUEST));
    jetty.context().addEventListener(new RequestListener());
    jetty.context().addEventListener(new ContextListener());
    jetty.context().getSessionHandler().addEventListener(new SessionListener());
    //<- HttpSessionListener needs to be added to SessionHandler.
  }
  private static void addWebsocketComponent(Jetty jetty)
    throws ServletException, DeploymentException
  {
    ServerContainer ws =
      WebSocketServerContainerInitializer.configureContext(jetty.context());
    ws.addEndpoint(ChatSocket.class);
  }
}