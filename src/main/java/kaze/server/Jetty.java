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
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import kaze.App;

public class Jetty {
  //-> settings
  ////-> thread
  private static int thMax=200, thMin=8, thTime=60000;
  public static void thread(int max, int min, int timeout) {
    thMax=max; thMin=min; thTime=timeout;
  }
  ////-> http
  private static int httpTime=30000;
  public static void http(int timeout) {
    httpTime=timeout;
  }
  ////-> http session (default: no timeout)
  public static void session(int timeoutSec) {
    appHand.setMaxInactiveInterval(timeoutSec);
  }
  ////-> static files
  public static void location(String classpath) {
    doc(Resource.newClassPathResource(classpath));
  }
  public static void location(File dir) {
    doc(Resource.newResource(dir));
  }
  private static void doc(Resource root) {
    rscHand = new ResourceHandler();
    rscHand.setDirectoriesListed(false);  // security
    rscHand.setBaseResource(root);
  }

  //-> handlers
  private static final AppHandler appHand = new AppHandler();
  private static ResourceHandler rscHand;

  //-> start
  public static void listen(int port) { listen(null, port); }
  public static void listen(String host, int port) {
    Server svr = new Server(
      new QueuedThreadPool(thMax, thMin, thTime)
    );
    HttpConfiguration conf = new HttpConfiguration();
    conf.setSendServerVersion(false);  // security
    ServerConnector http = new ServerConnector(
      svr, new HttpConnectionFactory(conf)
    );
    http.setHost(host);
    http.setPort(port);
    http.setIdleTimeout(httpTime);
    svr.addConnector(http);
    svr.setHandler(handlers());
    try {
      svr.start();
      svr.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private static HandlerList handlers() {
    HandlerList hands = new HandlerList();
    if (rscHand == null) {
      hands.setHandlers(new Handler[] {appHand});
    } else {
      hands.setHandlers(new Handler[] {appHand, rscHand});
    }
    return hands;
  }

  private static class AppHandler extends SessionHandler {
    @Override public void doHandle(
        String target, Request base,
        HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException
    {
      boolean run = false;
      try { run = App.run(req, res); }
      catch (Exception e) {
        res.sendError(500);
        throw new ServletException(e);
      }
      base.setHandled(run);
      super.doHandle(target, base, req, res);
    }
  }
}
