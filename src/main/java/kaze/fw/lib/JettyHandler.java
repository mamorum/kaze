package kaze.fw.lib;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import kaze.fw.Func;
import kaze.fw.Handler;
import kaze.fw.Routes;
import kaze.http.Req;
import kaze.http.Res;

public class JettyHandler extends AbstractHandler implements Handler {

	@Override  // AbstractHandler#handle, called by Jetty.
	public void handle(
		String target, Request baseReq,
		HttpServletRequest request,
		HttpServletResponse response
	) throws IOException, ServletException {
		baseReq.setHandled(true);
		handle(request, response);
	}

  public Routes routes;	
	
	public void listen() {
		Jetty server = new Jetty();
		server.start(this);
	}
	
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		StringBuilder access = new StringBuilder();

		String method = request.getMethod();
		String uri = request.getRequestURI();
		access.append(method).append(" ").append(uri);
		
		Func func = routes.get(method, uri);
		
		if (func == null) {
			response.setStatus(404);
			log(access, 404);
			return;
		}
		
		try {
			func.call(
				new Req(request, func.uriIndex),
				new Res(response)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		log(access, response.getStatus());
	}
	
	
	void log(StringBuilder s, int status) {
		System.out.println(s.append(" ").append(status));
	}
}
