package kaze;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import kaze.fw.Func;
import kaze.fw.Routes;
import kaze.fw.embed.JettyServer;
import kaze.fw.embed.JettyServlet;

public class App {
  
  public Conf conf; 

	public static void start(String... pkgs) {
	  Log.starts();
	  JettyServer jetty = new JettyServer(
	    config(),
	    new JettyServlet(routes(pkgs))
	  );
	  jetty.start();
	  Log.started();
	  jetty.listen();
	}

  public static Conf config() {
    InputStream s = App.class.getResourceAsStream("/config.yml");
    if (s == null) return new Conf();  // original setting.
    Yaml yaml = new Yaml();
    return yaml.loadAs(s, Conf.class);
    
//    return Jackson.toObj(yml, Conf.class);  // default setting.
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

  private static class Log {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static long start;
    static void starts() {
      start = System.currentTimeMillis();
      log.info("Starts");
    }
    static void started() {
      long started = System.currentTimeMillis();
      log.info(
          "Started in {}ms ( jvm uptime {}ms )",
          started - start,
          ManagementFactory
            .getRuntimeMXBean().getUptime()
      );
    }
  }  
}