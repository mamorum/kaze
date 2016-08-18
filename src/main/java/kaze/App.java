package kaze;

import java.lang.management.ManagementFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.fw.Conf;
import kaze.fw.Routes;
import kaze.fw.lib.Jetty;

public class App {
  
  public static Conf conf; 
  public static Routes routes;

	public static void start(String... pkgs) {
	  Log.starts();
	  build(pkgs);
	  Jetty.start();
	  Log.started();
	  Jetty.listen();
	}
	
	public static void build(String... pkgs) {
	  conf = Conf.build();
	  routes = Routes.build(pkgs);
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