package kaze.fw;

import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import kaze.fw.lib.JettyHandler;
import kaze.http.Route;

public class Build {
	
	// TODO support other handlers.
	public static Handler make(String... pkg) {
		try {
			Routes routes = scan(pkg);
			JettyHandler handler = new JettyHandler();
			handler.routes = routes;
			return handler;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Routes scan(String[] pkgs) throws Exception {
	  Routes routes = new Routes();
		for (String pkg : pkgs) scan(pkg, routes);
		return routes;
	}

	private static void scan(String pkg, Routes routes) throws Exception {
	  // TODO thread safe?
		Reflections ref = new Reflections(
		    pkg, new MethodAnnotationsScanner()
		);
		for (
		  Method m : ref.getMethodsAnnotatedWith(Route.class)
		) {
		  Route route = m.getAnnotation(Route.class);
		  String httpMethod = route.value()[0];
		  String httpUri = route.value()[1];
		  Func func = new Func(
		    m.getDeclaringClass().newInstance(), m
		  );
		  routes.add(httpMethod, httpUri, func);
		}
	}
}
