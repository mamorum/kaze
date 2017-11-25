package kaze.server;

import java.io.File;

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

public class Jetty {
  private Jetty() {};
  private static final QueuedThreadPool thread = new QueuedThreadPool();
  private static final Server server = new Server(thread);
  private static final HttpConfiguration httpconf = new HttpConfiguration();
  private static final ServerConnector connector = new ServerConnector(
    server, new HttpConnectionFactory(httpconf)
  );
  private static final ServletContextHandler context
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  private static final SessionHandler session = context.getSessionHandler();
  static {
    httpconf.setSendServerVersion(false);  // security
    server.addConnector(connector);
    server.setHandler(context);
  }
  //-> to setup
  public static QueuedThreadPool thread() { return thread; }
  public static ServerConnector connector() { return connector; }
  public static SessionHandler session() { return session; }
  public static ServletContextHandler context() { return context; }
  public static void app(App app, String publishPath) {
    ServletHolder sh = new ServletHolder(app.servlet());
    context.addServlet(sh, publishPath);
  }
  public static void doc(String classpathDir, String publishPath) {
    doc(Resource.newClassPathResource(classpathDir), publishPath);
  }
  public static void doc(File dir, String publishPath) {
    doc(Resource.newResource(dir), publishPath);
  }
  private static void doc(Resource staticFileDir, String publishPath) {
    context.setBaseResource(staticFileDir);
    ServletHolder sh = new ServletHolder(new DefaultServlet());
    sh.setInitParameter("dirAllowed", "false");  // security
    context.addServlet(sh, publishPath);
  }
  //-> to start
  public static void listen(int port) { listen(null, port); }
  public static void listen(String host, int port) {
    start(host, port);
    join();
  }
  private static void start(String host, int port) {
    connector.setHost(host);
    connector.setPort(port);
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
}
