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
	
	public <T> T paramsTo(Class<T> create) {
		try {
			T o = create.newInstance();
			// TODO populate parameters to obj.
			return o; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T jsonTo(Class<T> create) {
		try {
			return gson.fromJson(
				request.getReader(), create
			); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
