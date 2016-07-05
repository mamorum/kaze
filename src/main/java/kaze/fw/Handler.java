package kaze.fw;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import kaze.app.Http;
import kaze.app.Json;

public class Handler {

	// key = URI
	public Map<String, Action> get = new HashMap<>();
	
	public void handle(
			String target, Request baseReq,
			HttpServletRequest req, HttpServletResponse res
	) throws IOException {
		
		String uri = req.getRequestURI();
		String method = req.getMethod();
		System.out.println(method + " " + uri);
		
		Action action = get.get(uri);
		
		if (action == null) {
			res.setStatus(404);
			return;
		}
		
		Http arg = new Http(req, res);
		Object result = get.get(uri).invoke(arg);
		
		if (result instanceof Json) {
			res.setContentType("application/json;charset=utf-8");
	        res.setStatus(200);
			res.getWriter().print(((Json) result).toJson());
			return;
		}
	}	
}
