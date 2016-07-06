package kaze.fw;

import java.lang.reflect.Method;

import kaze.http.Req;
import kaze.http.Res;

public class Api {
	
	Object o; Method m;
	
	public Api(Object o, Method m) {
		this.o = o; this.m = m;
	}
	
	public Object invoke(Req req, Res res) {
		try { return m.invoke(o, req, res); }
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
