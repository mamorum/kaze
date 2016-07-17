package kaze.fw.lib;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import kaze.fw.Api;
import kaze.fw.Handler;
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

	// key = URI
	public Map<String, Api> get = null;	
	
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
		
		// TODO implement all methods.
		// now, All request methods go to @Get. 
		Api api = get.get(uri);
		
		if (api == null) {
			response.setStatus(404);
			log(access, 404);
			return;
		}
		
		try {
			get.get(uri).invoke(
				new Req(request), new Res(response)
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
