package kaze.http;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import kaze.fw.Jackson;

public class Req {

	public HttpServletRequest request;
	public Req(HttpServletRequest r) { this.request = r; }

	public String param(String name) {
		return  request.getParameter(name);
	}
	
	public <T> T params(Class<T> toObj) {
		try {
			T obj = toObj.newInstance();
			for (Field f : toObj.getDeclaredFields()) {
				setParam(obj, f, f.getName());
			}
			return obj;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setParam(Object o, Field f, String name) {
		// resolve
		Class<?> fType = f.getType();
		Object val = null;	
		if (
			fType.isArray() ||
			Collection.class.isAssignableFrom(fType)
		) {
			val = request.getParameterValues(name);
		}
		else {
			val = request.getParameter(name);
		}
		
		// no value in request
		if (val == null) return;
		
		// set
		try {
			f.setAccessible(true);
			f.set(o, Jackson.convert(val, fType));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T json(Class<T> toObj) {
		return Jackson.toObj(body(), toObj);
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
