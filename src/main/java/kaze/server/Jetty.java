package kaze.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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

public class Jetty {
  //-> settings
  ////-> location of static files
  private static Resource doc;
  public static void location(String classpathdir) {
    doc = Resource.newClassPathResource(classpathdir);
  }
  public static void location(File dir) {
    doc = Resource.newResource(dir);
  }
  ////-> http session (-1: default, no timeout)
  private static int ssnTimeSec = -1;
  public static void session(int timeoutSec) {
    ssnTimeSec=timeoutSec;
  }
  ////-> http connector
  private static int httpConTime=30000;
  public static void connector(int timeout) {
    httpConTime=timeout;
  }
  ////-> thread pool
  private static int thMax=200, thMin=8, thTime=60000;
  public static void thread(int max, int min, int timeout) {
    thMax=max; thMin=min; thTime=timeout;
  }

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
    svr.setHandler(servletHandler());
    try {
      svr.start();
      svr.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private static final String sla = "/";
  private static final ServletContextHandler servletHandler() {
    ServletContextHandler hnd
      = new ServletContextHandler(ServletContextHandler.SESSIONS);
    hnd.getSessionHandler().setMaxInactiveInterval(ssnTimeSec);
    hnd.setContextPath(sla);
    if (doc == null) {
      hnd.addServlet(AppServlet.class, sla);
    } else {
      ServletHolder shld = new ServletHolder(new AppDocServlet());
      shld.setInitParameter("dirAllowed", "false");  // security
      hnd.addServlet(shld, sla);
      hnd.setBaseResource(doc);
    }
    return hnd;
  }
  @SuppressWarnings("serial")  // TODO tomcat と共通化
  public static class AppServlet extends HttpServlet {
    @Override protected void service(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
      boolean run = App.run(req, res);
      if (!run) res.sendError(404);
    }
  }
  @SuppressWarnings("serial")
  private static class AppDocServlet extends DefaultServlet {
    @Override protected void service(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
      boolean run = App.run(req, res);
      if (!run) super.service(req, res); // static contents
    }
  }
}
