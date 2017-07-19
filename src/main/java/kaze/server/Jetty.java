package kaze.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import kaze.App;
import kaze.Route;
import kaze.Routes;

// TODO
// session関連
//  - session の設定（有効・無効)
//  - timeout
public class Jetty {
  //-> thread
  public int threadMax=200, threadMin=8, threadTime=60000;
  //-> connector (http)
  public String host=null;
  public int httpTime=30000;
  //-> location (doc root of static files)
  public Resource location;
  public void location(String classpath) {
    this.location = Resource.newClassPathResource(classpath);
  }
  public void location(File dir) {
    this.location = Resource.newResource(dir);
  }
  //-> app
  private App app;
  public Jetty() { super(); }
  public Jetty(App a) { this.app=a; }

  //-> start jetty
  public void listen(int port) {
    //-> thread
    Server sv = new Server(
      new QueuedThreadPool(threadMax, threadMin, threadTime)
    );
    //-> connector
    HttpConfiguration conf = new HttpConfiguration();
    conf.setSendServerVersion(false);  // security
    ServerConnector http = new ServerConnector(
      sv, new HttpConnectionFactory(conf)
    );
    http.setHost(host);
    http.setPort(port);
    http.setIdleTimeout(httpTime);
    sv.addConnector(http);
    //-> handler (location, app)
    HandlerList hands = new HandlerList();
    hands.setHandlers(handlers());
    sv.setHandler(hands);
    try {
      sv.start();
      sv.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Handler[] handlers() {
    ArrayList<Handler> list = new ArrayList<>(3);
    if (location != null) {
      ResourceHandler rhand = new ResourceHandler();
      rhand.setDirectoriesListed(false);  // security
      rhand.setBaseResource(location);
      list.add(rhand);
    }
    if (app != null) {
      list.add(new SessionHandler());
      list.add(new AppHandler());
    }
    if (list.isEmpty()) throw new IllegalStateException(
      // TODO message -> no contents to serve.
    );
    return list.toArray(new Handler[list.size()]);
  }

  private static class AppHandler extends AbstractHandler {
    @Override public void handle(
        String target, Request baseReq,
        HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
      String method = req.getMethod();
      Route r = Routes.plainUriRoute(method, target);
      if (r == null) r = Routes.regexUriRoute(method, target);
      if (r == null) return;  // not found
      r.run(req, res);
      baseReq.setHandled(true);
    }
  }
}
