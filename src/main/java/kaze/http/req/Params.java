package kaze.http.req;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import kaze.http.tool.Converter;

public class Params {
	
  private HttpServletRequest sr;
  private Params(HttpServletRequest sr) { this.sr = sr; }
  public static Params of(HttpServletRequest sr) {
    return new Params(sr);
  }
  
	public <T> T to(Class<T> to) {
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
	
	private void setParam(
	    Object o, Field f, String name
	) {
		// resolve
		Class<?> fType = f.getType();
		Object val = null;	
		if (
			fType.isArray() ||
			Collection.class.isAssignableFrom(fType)
		) {
			val = sr.getParameterValues(name);
		}
		else {
			val = sr.getParameter(name);
		}
		
		// no value in request
		if (val == null) return;
		
		// set
		try {
			f.setAccessible(true);
			f.set(o, Converter.convert(val, fType));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
