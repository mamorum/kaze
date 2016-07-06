package kaze.fw;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import kaze.http.Req;
import kaze.http.Res;

public class Handler {

	// key = URI
	public Map<String, Api> get = new HashMap<>();
	
	public void handle(
			String target, Request baseReq,
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		
		StringBuilder access = new StringBuilder();

		String method = request.getMethod();
		String uri = request.getRequestURI();
		access.append(method).append(" ").append(uri);
		
		Api api = get.get(uri);
		
		if (api == null) {
			response.setStatus(404);
			log(access, 404);
			return;
		}
		
		get.get(uri).invoke(
			new Req(request), new Res(response)
		);
		
		log(access, response.getStatus());
	}	
	
	void log(StringBuilder s, int status) {
		System.out.println(s.append(" ").append(status));
	}
}
