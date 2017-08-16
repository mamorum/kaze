package kaze.server;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

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
  //-> settings
  ////-> thread pool
  private static int thMax=200, thMin=8, thTime=60000;
  public static void thread(int max, int min, int timeout) {
    thMax=max; thMin=min; thTime=timeout;
  }
  ////-> http connector
  private static int httpConTime=30000;
  public static void connector(int timeout) {
    httpConTime=timeout;
  }
  ////-> http session (default: no timeout)
  public static void session(int timeoutSec) {
    handler.getSessionHandler().setMaxInactiveInterval(timeoutSec);
  }
  ////-> servlet & static files
  private static ServletHolder servlet;
  private static void servlet() {
    servlet = new ServletHolder(new App.Servlet());
  }
  private static void servlet(Resource doc) {
    servlet = new ServletHolder(new AppDocServlet());
    servlet.setInitParameter("dirAllowed", "false");  // security
    handler.setBaseResource(doc);
  }
  public static void location(String classpathdir) {
    servlet(Resource.newClassPathResource(classpathdir));
  }
  public static void location(File dir) {
    servlet(Resource.newResource(dir));
  }
  ////-> context path
  private static String ctxtpath = "/";
  public static void context(String path) {
    ctxtpath=path;
  }
  ////-> context handler
  private static final ServletContextHandler handler
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  public static ServletContextHandler handler() {
    return handler;
  }
  ////-> web socket
  static Consumer<ServletContextHandler> ws;

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
    http.setIdleTimeout(httpConTime);
    http.setHost(host);
    http.setPort(port);
    svr.addConnector(http);
    if (servlet == null) servlet();
    handler.addServlet(servlet, "/");
    handler.setContextPath(ctxtpath);
    svr.setHandler(handler);
    if (ws != null) ws.accept(handler);
    try {
      svr.start();
      svr.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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
