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
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import kaze.App;

// Embedded Jetty
public class Jetty {
  private static final QueuedThreadPool thread = new QueuedThreadPool();
  private static final Server server = new Server(thread);
  private static final HttpConfiguration httpconf = new HttpConfiguration();
  private static final ServerConnector connector = new ServerConnector(
    server, new HttpConnectionFactory(httpconf)
  );
  private static final ServletContextHandler context
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  private static final SessionHandler session
    = context.getSessionHandler();
  static {
    httpconf.setSendServerVersion(false);  // security
    server.setHandler(context);
  }
  //-> for settings
  public static QueuedThreadPool thread() { return thread; }
  public static ServerConnector connector() { return connector; }
  public static ServletContextHandler context() { return context; }
  public static SessionHandler session() { return session; }
  public static void location(String classpathdir) {
    root(Resource.newClassPathResource(classpathdir));
  }
  public static void location(File dir) {
    root(Resource.newResource(dir));
  }
  private static void root(Resource staticFileDir) {
    context.setBaseResource(staticFileDir);
    ServletHolder s = new ServletHolder(new DefaultServlet());
    s.setInitParameter("dirAllowed", "false");  // security
    context.addServlet(s, "/");
  }
  //-> start
  public static void listen(int port) { listen(null, port); }
  public static void listen(String host, int port) {
    start(host, port);
    join();
  }
  private static void start(String host, int port) {
    connector.setHost(host);
    connector.setPort(port);
    server.addConnector(connector);
//    context.addServlet(servlet(), "/");
    try { server.start();}
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private static void join() {
    try { server.join(); }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  //-> servlet
  private static ServletHolder servlet() {
    ServletHolder sh = new ServletHolder();
    if (server == null) { // mistake: fix -> if (location == null) ...
      sh.setServlet(new App.Servlet());
    } else {
      sh.setServlet(new AppDocServlet());

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
