package kaze.fw.embed;

import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.access.jetty.RequestLogImpl;
import kaze.App;
import kaze.fw.Config;

public class JettyServer {

  private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);
  
  Config.Jetty jetty = App.config.jetty;
  
  public void start() {
    logger.info("Jetty Config {}", jetty.toString());
    try {
      Server s = server();
      s.start();
      s.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

	private Server server() {
		QueuedThreadPool tp = new QueuedThreadPool(
		    jetty.threadMax, jetty.threadMin, jetty.threadTimeout
		);
    Server sv = new Server(tp);
    sv.setConnectors(connectors(sv));
    HandlerCollection hanc = new HandlerCollection();
    hanc.setHandlers(handlers());
    sv.setHandler(hanc);    
    return sv;
	}
	
	private Connector[] connectors(Server sv) {
    HttpConfiguration config = new HttpConfiguration();
    config.setSendServerVersion(false);  // always.
    HttpConnectionFactory conFcty = new HttpConnectionFactory(config);
    ServerConnector con = new ServerConnector(sv, conFcty);
    con.setHost(jetty.httpHost);
    con.setPort(port());
    con.setIdleTimeout(jetty.httpTimeout);
    return new Connector[] { con };
	}
	
	private int port() {
	  String port = System.getProperty("port");
	  if (port == null) return jetty.httpPort;
	  logger.info(
	      "Overwrite Jetty Config {}",
	      "[http: port=" + port + "]"
	  );
	  return Integer.parseInt(port);
	}
	
	private Handler[] handlers() {
    ServletContextHandler handler = new ServletContextHandler(
        ServletContextHandler.SESSIONS
    );
    Resource r = null;
    if (jetty.staticDir != null) {
      r = Resource.newResource(
          new File(jetty.staticDir)
      );
    } else {
      r =  Resource.newClassPathResource(
          jetty.staticPath
      );
    }
    handler.setBaseResource(r);
    handler.addServlet(svltHolder(), "/");

    RequestLogHandler logHandler = logHandler();
    
    return logHandler == null ?
        new Handler[]{handler} :
        new Handler[]{handler, logHandler};
	}
	
	private ServletHolder svltHolder() {
	  ServletHolder h = new ServletHolder(
	      "default", new JettyServlet()
	  );
    h.setInitParameter("dirAllowed", "false");  // always.
    return h;
	}

  // http://logback.qos.ch/access.html
	RequestLogHandler logHandler() {
	  String xml =  "/logback-access.xml";
	  if (this.getClass().getResource(xml) == null) return null;
    RequestLogImpl log = new RequestLogImpl();
    log.setResource(xml);
	  RequestLogHandler h = new RequestLogHandler();
    h.setRequestLog(log);
	  return h;
	}
}