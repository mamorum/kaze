package kaze.fw.lib;

import org.eclipse.jetty.server.Server;

public class Jetty {
	
	// TODO to be able to change port.
	public void start(JettyHandler handler) {
		
		Server server = new Server(8080);
		server.setHandler(handler);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}