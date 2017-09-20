package kaze.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import kaze.App;

// Embedded Jetty
public class Jetty {
  private static final QueuedThreadPool qtp
    = new QueuedThreadPool(200, 8, 60000);
  private static final ServletContextHandler handler
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  private static final Server server = new Server(qtp);
  static { server.setHandler(handler); }
  //-> for app
  public static ServletContextHandler handler() { return handler; }
  //-> settings
  ////-> thread
  public static void thread(int max, int min, int timeoutMill) {
    qtp.setMaxThreads(max);
    qtp.setMinThreads(min);
    qtp.setIdleTimeout(timeoutMill);
  }
  ////-> connector + session
  private static int httpConTime=30000;
  public static void http(int connectorTimeoutMill, int sessionTimeoutSec) {
    httpConTime = connectorTimeoutMill;
    handler.getSessionHandler().setMaxInactiveInterval(sessionTimeoutSec);
  }
  ////-> context path
  private static String ctxtpath = "/";
  public static void context(String path) {
    ctxtpath=path;
  }
  ////-> static files
  public static void location(String classpathdir) {
    handler.setBaseResource(Resource.newClassPathResource(classpathdir));
  }
  public static void location(File dir) {
    handler.setBaseResource(Resource.newResource(dir));
  }

  //-> start
  public static void listen(int port) { listen(null, port); }
  public static void listen(String host, int port) {
    HttpConfiguration conf = new HttpConfiguration();
    conf.setSendServerVersion(false);  // security
    ServerConnector http = new ServerConnector(
      server, new HttpConnectionFactory(conf)
    );
    http.setIdleTimeout(httpConTime);
    http.setHost(host);
    http.setPort(port);
    server.addConnector(http);
    handler.setContextPath(ctxtpath);
    handler.addServlet(servlet(), "/");
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  //-> servlet
  private static ServletHolder servlet() {
    ServletHolder sh = new ServletHolder();
    if (server == null) {
      sh.setServlet(new App.Servlet());
    } else {
      sh.setServlet(new AppDocServlet());
      sh.setInitParameter("dirAllowed", "false");  // security
    }
    return sh;
  }
  @SuppressWarnings("serial")
  public static class AppDocServlet extends DefaultServlet {
    @Override protected void doGet(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doGet(req, res);
      if (!done) super.doGet(req, res); // static contents
    }
    @Override protected void doPost(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doPost(req, res);
      if (!done) super.doPost(req, res); // static contents
    }
    @Override protected void doPut(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doPut(req, res);
      if (!done) res.sendError(404);
    }
    @Override protected void doDelete(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doDelete(req, res);
      if (!done) res.sendError(404);
    }
  }
}
