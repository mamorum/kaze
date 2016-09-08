package kaze.fw.lib;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.access.jetty.RequestLogImpl;
import kaze.App;
import kaze.Conf;
import kaze.fw.Route;
import kaze.fw.Routes;

public class Jetty {

  private static final Logger log = LoggerFactory.getLogger(Jetty.class);

  private static Server server;
  
  private static class Config {
    public static int
      httpPort = Conf.getInt("http.port"),
      httpTimeout = Conf.getInt("http.timeout"),
      threadMin = Conf.getInt("thread.min"),
      threadMax = Conf.getInt("thread.max"),
      threadTimeout = Conf.getInt("thread.timeout");
    public static String
      httpHost = Conf.get("http.host"),
      staticDir = Conf.get("static.dir"),
      staticPath = Conf.get("static.path");
    public static void log() { log.info("Server " + 
      "[http: host={}, port={}, timeout={}] " + 
      "[thread: min={}, max={}, timeout={}] " +
      "[static: dir={}, path={}]",
      httpHost, httpPort, httpTimeout,
      threadMin, threadMax, threadTimeout,
      staticDir, staticPath);
    }
  }
  
  public static void start() {
    Config.log();
    server = server();
    try { server.start(); }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void listen() {
    try { server.join(); }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static Server server() {
    QueuedThreadPool tp = new QueuedThreadPool(
        Config.threadMax, Config.threadMin, Config.threadTimeout
    );
    Server sv = new Server(tp);
    sv.setConnectors(Connectors.build(sv));
    HandlerCollection hc = new HandlerCollection();
    hc.setHandlers(Handlers.build());
    sv.setHandler(hc);
    return sv;
  }
	
	// Server Components ->
	private static class Connectors {
    static Connector[] build(Server sv) {
      HttpConfiguration c = new HttpConfiguration();
      c.setSendServerVersion(false);  // always.
      HttpConnectionFactory fac = new HttpConnectionFactory(c);
      ServerConnector con = new ServerConnector(sv, fac);
      con.setHost(Config.httpHost);
      con.setPort(Config.httpPort);
      con.setIdleTimeout(Config.httpTimeout);
      return new Connector[] { con };
    }
	}
	
	private static class Handlers {
    static Handler[] build() {
      Handler sh = ServletHandler.build();
      Handler lh = LogHandler.build();
      return (lh == null) ?
          new Handler[]{sh} :
          new Handler[]{sh, lh};
    }
	}
	
	private static class ServletHandler {
	  static Handler build() {
      ServletContextHandler h = new ServletContextHandler(
          ServletContextHandler.SESSIONS
      );
      h.setBaseResource(base());
      h.addServlet(servlet(), "/");
      h.setErrorHandler(ErrHandler.build());
      return h;
    }
    static Resource base() {
      if (Config.staticDir != null) {
        return Resource.newResource(
            new File(Config.staticDir)
        );
      }
      return Resource.newClassPathResource(
          Config.staticPath
      );
    } 
    static ServletHolder servlet() {
      ServletHolder h = new ServletHolder(
          "default", new Servlet()
      );
      // always.
      h.setInitParameter("dirAllowed", "false");
      return h;
    }
	}
	
	@SuppressWarnings("serial")
	private static class Servlet extends DefaultServlet {
	  private static Routes routes = App.routes;
	  protected void service(
	    HttpServletRequest sreq, HttpServletResponse sres)
	    throws ServletException, IOException
	  {
	    String m = sreq.getMethod();
	    String uri = sreq.getRequestURI();
	    Route route = routes.route(m, uri);    
	    if (route != null) route.run(uri, sreq, sres);
	    else super.service(sreq, sres);  // static contens
	  }
	}

  // see http://logback.qos.ch/access.html
	private static class LogHandler {
    static RequestLogHandler build() {
      String xml =  "/logback-access.xml";
      if (
        LogHandler.class.getResource(xml) == null
      ) return null;
      RequestLogImpl log = new RequestLogImpl();
      log.setQuiet(true);
      log.setResource(xml);
      RequestLogHandler h = new RequestLogHandler();
      h.setRequestLog(log);
      return h;
    }
	}
	
	private static class ErrHandler {
	  static ErrorHandler build() {
	    return new ErrorHandler() {
	      @Override public void handleErrorPage(
	        HttpServletRequest req, Writer writer,
	        int code, String msg) throws IOException
	      {
	        if (msg == null) {
	          msg = HttpStatus.getMessage(code);
	        }
	        writer.write(msg);
	      }
	    };
	  }
	}
}