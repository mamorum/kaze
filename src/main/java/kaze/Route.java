package kaze;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.route.Handler;
import kaze.route.Routes;

public class Route {

  public static Routes routes = new Routes();
  
  public static void build(String... pkgs) {
    if (pkgs == null) return; //return new Routes();
    if (pkgs.length == 0) return; //return new Routes();
    routes.scan(pkgs);
  }
  
  public static Handler handler(HttpServletRequest sreq) {
    String m = sreq.getMethod();
    String uri = sreq.getRequestURI();
    return routes.handler(m, uri);
  }
  
  // for container based servlet
  public static void run(
    HttpServletRequest sreq, HttpServletResponse sres
  ) {
    Handler f = handler(sreq);
    if (f != null) f.run(sreq, sres);
  }
}
