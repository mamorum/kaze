package kaze.route;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import kaze.Func;

public class Routes {
  
  private static HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  
  private static HashMap<String, HashMap<Pattern, Route>>
    method2regex = new HashMap<>();
  
  public static void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegex(method, uri, func);
    else addUri(method, uri, func);
  }
  
  private static void addRegex(String method, String uri, Func func) {
    HashMap<Pattern, Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new HashMap<>();
      method2regex.put(method, regexRoutes);
    }
    // Create URI index and, 
    // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
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
    Route route = new Route(func, uriIndex);
    Pattern regex = Pattern.compile(sb.toString());
    regexRoutes.put(regex, route);
  }

  private static void addUri(String method, String uri, Func func) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    Route route = new Route(func, null);
    uriRoutes.put(uri, route);
  }

  // for runtime.
  public static Route plainUriRoute(String method, String uri) {    
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) return null;
    return uriRoutes.get(uri);
  }  
  public static Route regexUriRoute(String method, String uri) {  
    HashMap<Pattern, Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) return null;
    for(Pattern r : regexRoutes.keySet()) {
      if (r.matcher(uri).matches()) return regexRoutes.get(r);
    }
    return null;  // not found
  }
}
