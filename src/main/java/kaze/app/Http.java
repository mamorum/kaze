package kaze.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Http {
	
	public HttpServletRequest req;
	public HttpServletResponse res;
	public Map<String, String[]> parameters;
	
	public Http(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
		this.parameters = req.getParameterMap();
	}
	
	public String param(String name) {
		String[] values = params(name);
		return  (values == null ? "" : values[0]);
	}
	
	public String[] params(String name) {
		return parameters.get(name);
	}
	
	public <T> T paramsTo(Class<T> form) {
		try {
			return form.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
