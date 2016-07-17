package kaze.fw;

import java.lang.reflect.Method;
import java.util.Map;

import kaze.http.Req;
import kaze.http.Res;

public class Func {
	
	public Object o; Method m;
	public Map<String, Integer> uriIndex;
	
	public Func(
	  Object o, Method m,
	  Map<String, Integer> uriIndex
	) {
		this.o = o; this.m = m;
		this.uriIndex = uriIndex;
	}
	
	public Object call(Req req, Res res) {
		try { return m.invoke(o, req, res); }
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
