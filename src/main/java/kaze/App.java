package kaze;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;

import kaze.app.Controller;
import kaze.app.method.Get;
import kaze.fw.Action;
import kaze.fw.Handler;
import kaze.fw.Jetty;

public class App {
	
	Handler handler = new Handler();
	
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
					handler.get.put(uri, a);
					System.out.println(uri +" -> " + obj.getClass().getName() + "#" + m.getName());
				}
			}
		}
	}
	
	public void start() {
		Jetty server = new Jetty();
		server.start(this.handler);
	}

}