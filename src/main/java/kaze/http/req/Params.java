package kaze.http.req;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import kaze.http.lib.Jackson;

public class Params {
		
	public static <T> Data<T> convert(
	    HttpServletRequest req, Class<T> to
	) {
	  return new Data<>(obj(req, to));
	}
	
	private static <T> T obj(
	    HttpServletRequest req, Class<T> to
	) {
		try {
			T o = to.newInstance();
			for (Field f : to.getDeclaredFields()) {
				setParam(req, o, f, f.getName());
			}
			return o;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void setParam(
	    HttpServletRequest req, 
	    Object o, Field f, String name
	) {
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
