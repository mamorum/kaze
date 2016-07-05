package kaze.fw;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Jetty extends AbstractHandler {
	
	// TODO to be able to change port.
	public void start(Handler handler) {

		this.handler = handler;
		
		Server server = new Server(8080);
		server.setHandler(this);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	Handler handler;
	
	@Override public void handle(
		String target, Request baseReq,
		HttpServletRequest req, HttpServletResponse res
	) throws IOException, ServletException {	
		baseReq.setHandled(true);
		handler.handle(target, baseReq, req, res);
	}
}