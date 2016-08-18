package kaze.fw;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.Http;

// not thread safe. 
public class Routes {

  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
  private HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  
  private HashMap<String, ArrayList<Route>>
    method2regex = new HashMap<>();
  
  // for fw init.
  private void add(String method, String uri, Func func) {
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
  public Route route(String method, String uri) {    
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
    
    // not found
    return null;
  }

  public static Routes build(String... pkgs) {
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
