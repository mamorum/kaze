package kaze.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaze.Route;

public class Routes {

//  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
  private static HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  
  private static HashMap<String, ArrayList<Route>>
    method2regex = new HashMap<>();
  
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
//    log(method, uri, func);
  }
  
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
  private static Route regexUriRoute(String method, String uri, Func func) {
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
//    log(method, uri, func);
  }

  private static Route uriRoute(String method, String uri, Func func) {
    Path path = new Path(uri);
    return new Route(method, path, func);
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
