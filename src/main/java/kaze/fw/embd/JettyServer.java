package kaze.fw.embd;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyServer {
	
  private JettyServlet s;
  public JettyServer(JettyServlet s) { this.s = s; }
  
	private Server server() {
    
	  ServletHolder holder = new ServletHolder("default", s);
    holder.setInitParameter("dirAllowed", "false");  // false, always.
	  
	  ServletContextHandler handler = new ServletContextHandler(
		    ServletContextHandler.SESSIONS
		);
    handler.setBaseResource(
        Resource.newClassPathResource("/public")
    );
		handler.addServlet(holder, "/");

		HttpConfiguration config = new HttpConfiguration();
		config.setSendServerVersion(false);  // false, always.
		HttpConnectionFactory conFcty = new HttpConnectionFactory(config);
		
		QueuedThreadPool pool = new QueuedThreadPool(
		    200, 8, 60000  // default -> max, min, idleTimeout
		);
    Server server = new Server(pool);
    ServerConnector con = new ServerConnector(server, conFcty);
    con.setHost(null);  // default is null.
    con.setPort(8080);  // default is 0, but overwrite always.
    con.setIdleTimeout(30000);  // default

    server.setHandler(handler);   
    server.setConnectors(new Connector[] { con });
    
    return server;
	}

  public void start() {
    try {
      Server s = server();
      s.start();
      s.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}