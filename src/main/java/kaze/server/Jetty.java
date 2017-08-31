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
  private final Server server;
  private final ServletContextHandler handler
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  //-> constractor + thread
  public Jetty() { this(200, 8, 60000); }
  public Jetty(int threadMax, int threadMin, int threadTimeoutMill) {
    server = new Server(
      new QueuedThreadPool(threadMax, threadMin, threadTimeoutMill)
    );
    server.setHandler(handler);
  }
  //-> for app
  public ServletContextHandler handler() {
    return handler;
  }
  //-> settings
  ////-> connector + session
  private int httpConTime=30000;
  public void http(int connectorTimeoutMill, int sessionTimeoutSec) {
    httpConTime = connectorTimeoutMill;
    handler.getSessionHandler().setMaxInactiveInterval(sessionTimeoutSec);
  }
  ////-> context path
  private  String ctxtpath = "/";
  public  void context(String path) {
    ctxtpath=path;
  }
  ////-> static files
  public void location(String classpathdir) {
    handler.setBaseResource(Resource.newClassPathResource(classpathdir));
  }
  public void location(File dir) {
    handler.setBaseResource(Resource.newResource(dir));
  }

  //-> start
  public void listen(int port) { listen(null, port); }
  public void listen(String host, int port) {
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
  private ServletHolder servlet() {
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
