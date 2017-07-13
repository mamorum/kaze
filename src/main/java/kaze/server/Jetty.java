package kaze.server;

import java.io.File;
import java.io.IOException;

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
import kaze.Routes;

public class Jetty {
  App app;
  public Jetty() { super(); }
  public Jetty(App a) { app=a; }
  //-> http
  public String host=null;
  public int httpTime=30000;
  //-> thread
  public int max=200, min=8, threadTime=60000;
  //-> static files
  private String path=null;
  private File dir=null;

  //-> server settings
  public void http(String host, int timeout) {
    this.host = host;
    this.httpTime = timeout;
  }
  public void thread(int max, int min, int timeout) {
    this.max = max;
    this.min = min;
    this.threadTime = timeout;
  }
  public void location(String classpath) { this.path = classpath; }
  public void location(File dir) { this.dir = dir;}

  public void listen(int port) {
    Server sv = new Server(
      new QueuedThreadPool(max, min, threadTime)
    );
    //-> connector
    HttpConfiguration conf = new HttpConfiguration();
    conf.setSendServerVersion(false);  // security
    ServerConnector http = new ServerConnector(
      sv, new HttpConnectionFactory(conf)
    );
    if (host != null) http.setHost(host);
    http.setPort(port);
    http.setIdleTimeout(httpTime);
    sv.addConnector(http);
    //-> handler
    ResourceHandler rhand = new ResourceHandler();
    rhand.setDirectoriesListed(false);  // security
    rhand.setBaseResource(location(path, dir));
    HandlerList hands = new HandlerList();
    hands.setHandlers(new Handler[] {
      new SessionHandler(), rhand, new AppHandler()
    });
    sv.setHandler(hands);
    try {
      sv.start();
      sv.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private static Resource location(String path, File dir) {
    if (path != null) return Resource.newClassPathResource(path);
    else if (dir != null) return Resource.newResource(dir);
    else return null;
  }

  private static class AppHandler extends AbstractHandler {
    @Override public void handle(
        String target, Request baseReq,
        HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
      String method = req.getMethod();
      Route r = Routes.plainUriRoute(method, target);
      if (r == null) r = Routes.regexUriRoute(method, target);
      if (r == null) return;  // not found
      r.run(req, res);
      baseReq.setHandled(true);
    }
  }
}
