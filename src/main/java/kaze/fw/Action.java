package kaze.fw;

import java.lang.reflect.Method;

import kaze.app.Http;

public class Action {
	
	Object o; Method m;
	
	public Action(Object o, Method m) {
		this.o = o; this.m = m;
	}
	
	public Object invoke(Http arg) {
		try { return m.invoke(o, arg); }
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
