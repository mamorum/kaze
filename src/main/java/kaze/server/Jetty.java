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
  //-> to customize jetty
  public static final QueuedThreadPool thread = new QueuedThreadPool();
  public static final Server server = new Server(thread);
  public static final HttpConfiguration httpconf = new HttpConfiguration();
  public static final ServerConnector connector = new ServerConnector(
    server, new HttpConnectionFactory(httpconf)
  );
  public static final ServletContextHandler context
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  public static final SessionHandler session = context.getSessionHandler();
  static {
    httpconf.setSendServerVersion(false); // security
    server.addConnector(connector);
    server.setHandler(context);
  }
  //-> to publish app
  public static void app(App app, String publishPath) {
    ServletHolder sh = new ServletHolder(app);
    context.addServlet(sh, publishPath);
  }
  //-> to publish static files
  public static void doc(String classpathDir, String publishPath) {
    doc(Resource.newClassPathResource(classpathDir), publishPath);
  }
  public static void doc(File fileSystemDir, String publishPath) {
    doc(Resource.newResource(fileSystemDir), publishPath);
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
