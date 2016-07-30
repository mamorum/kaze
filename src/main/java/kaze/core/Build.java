package kaze.core;

import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import kaze.Http;
import kaze.core.embed.JettyServer;
import kaze.core.embed.JettyServlet;

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
		  Method m : ref.getMethodsAnnotatedWith(Http.class)
		) {
		  Http http = m.getAnnotation(Http.class);
		  String httpMethod = http.value()[0];
		  String httpUri = http.value()[1];
		  Func func = new Func(
		    m.getDeclaringClass().newInstance(), m
		  );
		  routes.add(httpMethod, httpUri, func);
		}
	}
}
