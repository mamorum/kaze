package kaze.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import kaze.App;
import kaze.Route;

public class Jetty {
  //-> settings
  ////-> app
  private App app;
  public Jetty() { super(); }
  public Jetty(App a) { this.app=a; }
  ////-> thread
  private int thMax=200, thMin=8, thTime=60000;
  public void thread(int max, int min, int timeout) {
    thMax=max; thMin=min; thTime=timeout;
  }
  ////-> http
  private int httpTime=30000;
  public void http(int timeout) { httpTime=timeout; }
  ////-> session
  private SessionHandler shand;
  public void session(int timeoutSec) {  // -1: no timeout
    shand = new SessionHandler();
    shand.setMaxInactiveInterval(timeoutSec);
  }
  ////-> static files
  private ResourceHandler rhand;
  private void location(Resource root) {
    rhand = new ResourceHandler();
    rhand.setDirectoriesListed(false);  // security
    rhand.setBaseResource(root);
  }
  public void location(String classpath) {
    location(Resource.newClassPathResource(classpath));
  }
  public void location(File dir) {
    location(Resource.newResource(dir));
  }

  //-> start
  public void listen(int port) { listen(null, port); }
  public void listen(String host, int port) {
    Server sv = new Server(
      new QueuedThreadPool(thMax, thMin, thTime)
    );
    HttpConfiguration conf = new HttpConfiguration();
    conf.setSendServerVersion(false);  // security
    ServerConnector http = new ServerConnector(
      sv, new HttpConnectionFactory(conf)
    );
    http.setHost(host);
    http.setPort(port);
    http.setIdleTimeout(httpTime);
    sv.addConnector(http);
    sv.setHandler(handlers());
    try { sv.start(); sv.join(); }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private HandlerList handlers() {
    ArrayList<Handler> list = new ArrayList<>(3);
    if (rhand != null) list.add(rhand);
    if (shand != null) list.add(shand);
    if (app != null) list.add(new WebApp(app));
    HandlerList hands = new HandlerList();
    hands.setHandlers(
      list.toArray(new Handler[list.size()])
    );
    return hands;
  }
  private static class WebApp extends AbstractHandler {
    private App app;
    public WebApp(App app) { this.app=app; }
    @Override public void handle(
        String target, Request baseReq,
        HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
      String method = req.getMethod();
      Route r = app.routes.plainUriRoute(method, target);
      if (r == null) {
        r = app.routes.regexUriRoute(method, target);
        if (r == null) return;  // not found
      }
      r.run(req, res);
      baseReq.setHandled(true);
    }
  }
}
