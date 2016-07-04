package kaze.fw.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import kaze.App;

public class JettyServer extends AbstractHandler {
	
	// TODO to be able to change port.
	public void start(App app) {

		this.app = app;
		
		Server server = new Server(8080);
		server.setHandler(this);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private App app;
	
	@Override public void handle(
		String target, Request baseReq,
		HttpServletRequest req, HttpServletResponse res
	) throws IOException, ServletException {	
		baseReq.setHandled(true);
		app.handle(target, baseReq, req, res);
	}
}
