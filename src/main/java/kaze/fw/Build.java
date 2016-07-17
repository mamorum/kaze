package kaze.fw;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import kaze.fw.lib.JettyHandler;
import kaze.http.WebApi;
import kaze.http.method.Connect;
import kaze.http.method.Delete;
import kaze.http.method.Get;
import kaze.http.method.Head;
import kaze.http.method.Options;
import kaze.http.method.Post;
import kaze.http.method.Put;
import kaze.http.method.Trace;

public class Build {

	private static final Class<?>[] httpMethods = {
		Get.class, Head.class, Post.class, Put.class,
		Delete.class, Connect.class, Options.class, Trace.class
	};

	private static final
		Map<String, Map<String, Api>> apiMap = new HashMap<>();
	
	private static final
		String[] httpMethodNames = new String[httpMethods.length];
	
	static {
		initFields();
	}
	
	private static void initFields() {
		for (int i = 0; i < httpMethods.length; i++) {
			String name = name(httpMethods[i]);
			apiMap.put(name, new HashMap<>());
			httpMethodNames[i] = name;
		}
	}
	
	private static String name(Class<?> c) {
		return c.getSimpleName().toUpperCase();
	}
	
	// TODO support other handlers.
	// TODO support all packages.
	public Handler make(String... pkg) {
		try {
			scan(pkg); 
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		JettyHandler handler = new JettyHandler();
//		handler.get = 
		return handler;
	}

	private void scan(String[] pkgs) throws Exception {
		for (String pkg : pkgs) scan(pkg);
	}

	private void scan(String pkg) throws Exception {
				
		Reflections ref = new Reflections(pkg);
		Set<Class<?>> classes = ref.getTypesAnnotatedWith(WebApi.class);
		
		for (Class<?> cls : classes) {
			Object obj = cls.newInstance();
			
			for (Method classMethod : cls.getMethods()) {
				
				for (int i = 0; i < httpMethods.length; i++) {
					Map<String, Api> uri2api = apiMap.get(httpMethodNames[i]);
					Class<?> acls = httpMethods[i];
					Get atGet = classMethod.getAnnotation(Get.class);
					
					if (atGet != null) {
						String uri = atGet.value();
						Api a = new Api(obj, classMethod);
						uri2api.put(uri, a);
						System.out.println(uri +" -> " + obj.getClass().getName() + "#" + classMethod.getName());
					}

				}								
			}
		}
	}
}
