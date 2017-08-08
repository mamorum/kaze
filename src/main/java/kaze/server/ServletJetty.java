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

public class ServletJetty {
  //-> handler + servlet
  private static final ServletContextHandler handler
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  private static final ServletHolder servlet
    = new ServletHolder(new AppServlet());
  static {
    servlet.setInitParameter("dirAllowed", "false");
    handler.setContextPath("/");
    handler.addServlet(servlet, "/");
  }

  //-> settings
  ////-> location of static files
  private static void doc(Resource rsc) {
    handler.setBaseResource(rsc);
  }
  public static void location(String classpathdir) {
    doc(Resource.newClassPathResource(classpathdir));
  }
  public static void location(File dir) {
    doc(Resource.newResource(dir));
  }
  ////-> http session (default: no timeout)
  public static void session(int timeoutSec) {
    handler.getSessionHandler().setMaxInactiveInterval(timeoutSec);
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
    svr.setHandler(handler);
    try {
      svr.start();
      svr.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("serial")
  private static class AppServlet extends DefaultServlet {
    @Override protected void service(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
      try {
        boolean run = App.run(req, res);
        if (!run) super.service(req, res);  // static contens
      } catch (Exception e) {
        res.sendError(500);
        throw new ServletException(e);
      }
    }
  }
}
