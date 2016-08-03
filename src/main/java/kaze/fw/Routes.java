package kaze.fw;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.ex.NoRouteException;

// not thread safe. 
public class Routes {

  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
  private HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  
  private HashMap<String, ArrayList<Route>>
    method2regex = new HashMap<>();
  
  // for fw init.
  public void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegex(method, uri, func);
    else addUri(method, uri, func);
  }
  
  private void addRegex(String method, String uri, Func func) {
    ArrayList<Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new ArrayList<>();
      method2regex.put(method, regexRoutes);
    }
    Route route = Route.fromRegexUri(uri, func);
    regexRoutes.add(route);
    log(method, uri, func);
  }

  private void addUri(String method, String uri, Func func) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    Route route = Route.fromUri(uri, func);
    uriRoutes.put(uri, route);
    log(method, uri, func);
  }

  private void log(String method, String uri, Func f) {
    logger.info(
        "[{} {}] -> [{}#{}]", method, uri, 
        f.m.getDeclaringClass().getName(), 
        f.m.getName());
  }

  // for app running.
  public void run(
      HttpServletRequest sreq,
      HttpServletResponse sres
  ) {
    String m = sreq.getMethod();
    String uri = sreq.getRequestURI();
    Route route = route(m, uri);
    route.run(uri, sreq, sres);
  }
  
  private Route route(String method, String uri) {    
    // resolve from method2uri
    Route route = null;
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes != null) route = uriRoutes.get(uri);
    if (route != null) return route;
    
    // resolve from method2regex
    ArrayList<Route> regexRoutes = method2regex.get(method); 
    if (regexRoutes != null) {
      for(Route r : regexRoutes) {
        if (r.uriPattern.matcher(uri).matches()) return r;
      }
    }    
    throw new NoRouteException(
        "Route not found. [" + method + " " + uri + "]"
    );
  }
}
