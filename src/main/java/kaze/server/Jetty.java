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
  private final QueuedThreadPool thread = new QueuedThreadPool();
  private final Server server = new Server(thread);
  private final HttpConfiguration httpconf = new HttpConfiguration();
  private final ServerConnector connector = new ServerConnector(
    server, new HttpConnectionFactory(httpconf)
  );
  private final ServletContextHandler context
    = new ServletContextHandler(ServletContextHandler.SESSIONS);
  private final SessionHandler session = context.getSessionHandler();
  public Jetty() {
    httpconf.setSendServerVersion(false);  // security
    server.setHandler(context);
  }
  //-> to setup
  public QueuedThreadPool thread() { return thread; }
  public ServerConnector connector() { return connector; }
  public SessionHandler session() { return session; }
  public ServletContextHandler context() { return context; }
  public void app(App app, String publishPath) {
    ServletHolder sh = new ServletHolder(app.servlet());
    context.addServlet(sh, publishPath);
  }
  public void doc(String classpathDir, String publishPath) {
    doc(Resource.newClassPathResource(classpathDir), publishPath);
  }
  public void doc(File dir, String publishPath) {
    doc(Resource.newResource(dir), publishPath);
  }
  private void doc(Resource staticFileDir, String publishPath) {
    context.setBaseResource(staticFileDir);
    ServletHolder sh = new ServletHolder(new DefaultServlet());
    sh.setInitParameter("dirAllowed", "false");  // security
    context.addServlet(sh, publishPath);
  }
  //-> to start
  public void listen(int port) { listen(null, port); }
  public void listen(String host, int port) {
    start(host, port);
    join();
  }
  private void start(String host, int port) {
    connector.setHost(host);
    connector.setPort(port);
    server.addConnector(connector);
    try { server.start();}
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private void join() {
    try { server.join(); }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
