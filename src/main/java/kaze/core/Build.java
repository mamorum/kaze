package kaze.core;

import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import kaze.Http;
import kaze.core.embed.JettyServer;
import kaze.core.embed.JettyServlet;

public class Build {
	
  public JettyServer server(String... pkgs) {
    return new JettyServer(
        new JettyServlet(routes(pkgs))
    );
  }
  
  public Routes routes(String... pkgs) {
    return scan(pkgs);
  }

	private Routes scan(String... pkgs) {
	  Reflections ref = new Reflections(
		    pkgs, new MethodAnnotationsScanner()
		);
	  Routes routes = new Routes();
		for (
		  Method m : ref.getMethodsAnnotatedWith(Http.class)
		) {
		  Http http = m.getAnnotation(Http.class);
		  String httpMethod = http.value()[0].toUpperCase();
		  String httpUri = http.value()[1];
		  Func func = Func.of(m);
		  routes.add(httpMethod, httpUri, func);
		}
		return routes;
	}
}
