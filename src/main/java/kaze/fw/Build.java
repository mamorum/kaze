package kaze.fw;

import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import kaze.fw.lib.JettyServer;
import kaze.fw.lib.JettyServlet;
import kaze.http.Route;

public class Build {
	
  public JettyServer server(String[] pkg) {
    return new JettyServer(
        new JettyServlet(routes(pkg))
    );
  }
  
	public Routes routes(String... pkg) {
		try {
			return scan(pkg);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Routes scan(String... pkgs) throws Exception {
	  Routes routes = new Routes();
		for (String pkg : pkgs) scan(pkg, routes);
		return routes;
	}

	private void scan(String pkg, Routes routes) throws Exception {
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
