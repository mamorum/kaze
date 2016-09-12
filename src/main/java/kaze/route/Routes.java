package kaze.route;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.Http;
import kaze.Route;

public class Routes {

  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
  private static HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  
  private static HashMap<String, ArrayList<Route>>
    method2regex = new HashMap<>();
  
  // for init.
  public static void build(String... pkgs) {
    if (pkgs == null) return;
    if (pkgs.length == 0) return;
    Reflections ref = new Reflections(
        pkgs, new MethodAnnotationsScanner()
    );
    for (
      Method m : ref.getMethodsAnnotatedWith(Http.class)
    ) {
      Http http = m.getAnnotation(Http.class);
      String httpMethod = http.value()[0].toUpperCase();
      String httpUri = http.value()[1];
      Func func = Func.of(m);
      add(httpMethod, httpUri, func);
    }
  }
  
  public static void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegex(method, uri, func);
    else addUri(method, uri, func);
  }
  
  private static void addRegex(String method, String uri, Func func) {
    ArrayList<Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new ArrayList<>();
      method2regex.put(method, regexRoutes);
    }
    Route route = regexUriRoute(method, uri, func);
    regexRoutes.add(route);
    log(method, uri, func);
  }
  
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
  public static Route regexUriRoute(String method, String uri, Func func) {
    Map<String, Integer> uriIndex = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    String[] path = uri.substring(1).split("/");
    for (int i = 0; i < path.length; i++) {
      sb.append("/");
      if (path[i].contains(":")) {
        sb.append("[^/]+");
        uriIndex.put(path[i], i);
      } else {
        sb.append(path[i]);
      }
    }
    if (uri.endsWith("/")) sb.append("/");
    
    String regexUri = sb.toString();
    Path p = new Path(regexUri, uriIndex);
    return new Route(method, p, func);
  }

  private static void addUri(String method, String uri, Func func) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    Route route = uriRoute(method, uri, func);
    uriRoutes.put(uri, route);
    log(method, uri, func);
  }

  private static Route uriRoute(String method, String uri, Func func) {
    Path path = new Path(uri);
    return new Route(method, path, func);
  }

  private static void log(String method, String uri, Func f) {
    logger.info(
        "[{} {}] -> [{}#{}]", method, uri, 
        f.m.getDeclaringClass().getName(), 
        f.m.getName());
  }

  // for runtime.
  public static Route get(String method, String uri) {    
    // resolve from method2uri
    Route route = null;
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes != null) route = uriRoutes.get(uri);
    if (route != null) return route;
    
    // resolve from method2regex
    ArrayList<Route> regexRoutes = method2regex.get(method); 
    if (regexRoutes != null) {
      for(Route r : regexRoutes) {
        if (r.path.uriPattern.matcher(uri).matches()) return r;
      }
    }
    
    // not found
    return null;
  }
}
