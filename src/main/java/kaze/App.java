package kaze;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.reflections.Reflections;

import kaze.app.Controller;
import kaze.app.Http;
import kaze.app.Json;
import kaze.app.method.Get;
import kaze.fw.Action;
import kaze.fw.Jetty;

public class App {
	
	private Map<String, Action> get = new HashMap<>();
	
	public static App build(String pkg) {
		App app = new App();
		try {
			app.scan(pkg);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return app;
	}
	
	private void scan(String pkg) throws Exception {
		
		Reflections ref = new Reflections(pkg);
		Set<Class<?>> classes = ref.getTypesAnnotatedWith(Controller.class);
		
		for (Class<?> c : classes) {
			Object obj = c.newInstance();
			
			for (Method m : c.getMethods()) {
				Get atGet = m.getAnnotation(Get.class);
				
				if (atGet != null) {
					String uri = atGet.value();
					Action a = new Action(obj, m);
					get.put(uri, a);
					System.out.println(uri +" -> " + obj.getClass().getName() + "#" + m.getName());
				}
			}
		}
	}
	
	public void start() {
		Jetty server = new Jetty();
		server.start(this);
	}

	public void handle(
			String target, Request baseReq,
			HttpServletRequest req, HttpServletResponse res
	) throws IOException {
		
		String uri = req.getRequestURI();
		String method = req.getMethod();
		System.out.println(method + " " + uri);
		
		Action action = get.get(uri);
		
		if (action == null) {
			res.setStatus(404);
			return;
		}
		
		Http arg = new Http(req, res);
		Object result = get.get(uri).invoke(arg);
		
		if (result instanceof Json) {
			res.setContentType("application/json;charset=utf-8");
	        res.setStatus(200);
			res.getWriter().print(((Json) result).toJson());
			return;
		}
	}	
}