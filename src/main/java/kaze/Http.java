package kaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.lib.Jetty;
import kaze.route.Func;
import kaze.route.Routes;

public class Http {
  private static Http srv = new Http();
  public static Http server() { return srv; }

  public Http get(String uri, Func f) { return add("GET", uri, f); }
  public Http post(String uri, Func f) { return add("POST", uri, f); }

  private Http add(String method, String uri, Func f) {
    Routes.add(method, uri, f);
    return this; 
  }
  
  public void listen() {
    Watch.start();
    Jetty.start();
    Watch.stop();
    Jetty.listen();
  }
  
  private static final Logger log = LoggerFactory.getLogger(Http.class);
  private static class Watch {
    private static long stime;
    static void start() {
      stime = System.currentTimeMillis();
    }
    static void stop() {
      long started = System.currentTimeMillis();
      log.info("Started in {}ms", started - stime);
    }
  }
}
