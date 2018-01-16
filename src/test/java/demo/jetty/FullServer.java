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
    app.get.add("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    app.get.add("/err", (req, res) -> {
      throw new Exception("/err");
    });
    app.get.add("/ssn", (req, res) -> {
      HttpSession ss = req.srv.getSession(true);
      if (ss.isNew()) res.json("isNew", true);
      else res.json("isNew", false);;
    });
    Gson gson = new Gson();
    app.conf.parser(gson::fromJson, gson::toJson);
    //-> Jetty settings
    Jetty.thread.setMaxThreads(20);
    Jetty.thread.setMinThreads(20);
    Jetty.thread.setIdleTimeout(500000);  //<- msec
    Jetty.connector.setIdleTimeout(60000);  //<- msec
    Jetty.session.setMaxInactiveInterval(
      300 //<- session timeout sec (300=5min)
    );
    Jetty.context.setContextPath("/");
    addServletComponent();
    addWebsocketComponent();
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    Jetty.listen("0.0.0.0", 8080);
  }
  private static void addServletComponent() {
    ServletHolder sh = new ServletHolder(new HelloServlet());
    sh.setAsyncSupported(true);
    // TODO set async servlet.
    Jetty.context.addServlet(HelloServlet.class, "/hello/*");
    Jetty.context.addFilter(HelloLogFilter.class, "/hello", EnumSet.of(DispatcherType.REQUEST));
    Jetty.context.addEventListener(new RequestListener());
    Jetty.context.addEventListener(new ContextListener());
    Jetty.session.addEventListener(new SessionListener());
    //<- HttpSessionListener needs to be added to SessionHandler.
  }
  private static void addWebsocketComponent()
    throws ServletException, DeploymentException
  {
    ServerContainer ws =
      WebSocketServerContainerInitializer.configureContext(Jetty.context);
    ws.addEndpoint(ChatSocket.class);
  }
}