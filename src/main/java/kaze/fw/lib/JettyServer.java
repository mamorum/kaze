package kaze.fw.lib;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

public class JettyServer {
	
  private JettyServlet s;
  public JettyServer(JettyServlet s) { this.s = s; }
  
	// TODO to be able to change port.
	public void start() {
		
		ServletContextHandler h = new ServletContextHandler(
		    ServletContextHandler.SESSIONS
		);
    h.addServlet(new ServletHolder("default", s), "/");
		h.setBaseResource(Resource.newClassPathResource("/public"));
		
    Server server = new Server(8080);
    server.setHandler(h);   
    
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}