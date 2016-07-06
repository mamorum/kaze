package kaze.http;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class Req {

	static final Gson gson = new Gson();
	public HttpServletRequest request;
	public Req(HttpServletRequest r) { this.request = r; }

	public String param(String name) {
		String[] values = params(name);
		return  (values == null ? "" : values[0]);
	}
	
	public String[] params(String name) {
		return request.getParameterMap().get(name);
	}
	
	public <T> T params(Class<T> dist) {
		try {
			// TODO populate parameters to obj.
			return dist.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T json(Class<T> dist) {
		try {
			return gson.fromJson(
				request.getReader(), dist
			); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
