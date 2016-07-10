package kaze.http.request;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import kaze.fw.Jackson;

public class Param {

	private HttpServletRequest req;	
	public Param(HttpServletRequest req) {
		this.req = req;
	}
	
	public String val(String name) {
		return req.getParameter(name);
	}

	public String[] vals(String name) {
		return req.getParameterValues(name);
	}
	
	public <T> T convert(String name, Class<T> to) {
		return Jackson.convert(val(name), to);
	}
	
	public <T> Data<T> bind(Class<T> to) {
		return new Data<>(toObj(to));
	}
	
	private <T> T toObj(Class<T> to) {
		try {
			T o = to.newInstance();
			for (Field f : to.getDeclaredFields()) {
				setParam(o, f, f.getName());
			}
			return o;
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
			val = req.getParameterValues(name);
		}
		else {
			val = req.getParameter(name);
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
}
