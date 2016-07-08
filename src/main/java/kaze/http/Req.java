package kaze.http;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import kaze.http.util.Json;

public class Req {

	public HttpServletRequest request;
	public Req(HttpServletRequest r) { this.request = r; }

	public String param(String name) {
		return  request.getParameter(name);
	}
	
	public <T> T params(Class<T> toObj) {
		try {
			// TODO populate parameters to obj.
			return toObj.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T json(Class<T> toObj) {
		return Json.toObj(body(), toObj);
	}
	
	// TODO check program action, when i close the reader.
	private String body() {
		try {			
			if (request.getCharacterEncoding() == null) {
				request.setCharacterEncoding("utf-8");
			}			
			BufferedReader r = request.getReader();
			StringBuilder body = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				body.append(line);
			}
			return body.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
