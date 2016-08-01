package kaze;

import java.lang.reflect.Method;
import java.net.URL;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import kaze.data.Tool;
import kaze.fw.Config;
import kaze.fw.Func;
import kaze.fw.Routes;
import kaze.fw.embed.JettyServer;
import kaze.fw.embed.JettyServlet;

public class App {
  
	public static void start(String... pkgs) {
	  Config c = config();
	  Routes r = routes(pkgs);
	  (new JettyServer(
	      c, new JettyServlet(r)
	  )).start();
	}

  public static Config config() {
    URL json = App.class.getResource("/config.json");
    if (json == null) return Config.defaults();
    return Tool.toObj(json, Config.class);
  }	
  
  public static Routes routes(String... pkgs) {
    return scan(pkgs);
  }
  
  private static Routes scan(String... pkgs) {
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