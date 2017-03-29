package kaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.lib.Jetty;
import kaze.route.Func;
import kaze.route.Routes;

public class Http {
  private static final Logger log = LoggerFactory.getLogger(Http.class);
  
  private static final Http h = new Http();
  public static Http server() { return h; }

  public void listen() {
    Jetty.start();
    log.info("Started");
    Jetty.listen();
  }

  // See "org.eclipse.jetty.http.HttpMethod"
  public Http get(String uri, Func f) { return add("GET", uri, f); }
  public Http post(String uri, Func f) { return add("POST", uri, f); }
  public Http head(String uri, Func f) { return add("HEAD", uri, f); }
  public Http put(String uri, Func f) { return add("PUT", uri, f); }
  public Http options(String uri, Func f) { return add("OPTIONS", uri, f); }
  public Http delete(String uri, Func f) { return add("DELETE", uri, f); }
  public Http trace(String uri, Func f) { return add("TRACE", uri, f); }
  public Http connect(String uri, Func f) { return add("CONNECT", uri, f); }
  public Http move(String uri, Func f) { return add("MOVE", uri, f); }
  public Http proxy(String uri, Func f) { return add("PROXY", uri, f); }
  public Http pri(String uri, Func f) { return add("PRI", uri, f); }

  private Http add(String method, String uri, Func f) {
    Routes.add(method, uri, f);
    return this; 
  }  
}
