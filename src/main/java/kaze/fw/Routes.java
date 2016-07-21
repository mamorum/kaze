package kaze.fw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// not thread safe. single thread consumes this class.
public class Routes {

  public HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  
  public HashMap<String, ArrayList<Route>>
    method2regex = new HashMap<>();
  
  // for fw init.
  public void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegex(method, uri, func);
    else addUri(method, uri, func);
  }
  
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
  private void addRegex(String method, String uri, Func func) {
    
    Map<String, Integer> index = new HashMap<>();
    StringBuilder regex = new StringBuilder();
    String[] path = uri.substring(1).split("/");
    for (int i = 0; i < path.length; i++) {
      regex.append("/");
      if (path[i].contains(":")) {
        regex.append("[^/]+");
        index.put(path[i], i);
      } else {
        regex.append(path[i]);
      }
    }
    if (uri.endsWith("/")) regex.append("/");

    Pattern pattern = Pattern.compile(regex.toString());
    addRegexRoute(
        method, new Route(func, pattern, index)
    );
//    System.out.println(
//        method + " " + regex.toString().replaceAll("\\[\\^/\\]\\+", "*") +
//        " -> " + func.m.getDeclaringClass().getSimpleName() +
//        "#" + func.m.getName()
//    );
  }
  
  private void addRegexRoute(String method, Route route) {
    ArrayList<Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new ArrayList<>();
      method2regex.put(method, regexRoutes);
    }
    regexRoutes.add(route);    
  }

  private void addUri(String method, String uri, Func func) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    uriRoutes.put(uri, new Route(func));    
    
//    System.out.println(
//        method + " " + uri +
//        " -> " + func.m.getDeclaringClass().getSimpleName() +
//        "#" + func.m.getName()
//    );
  }

  // for app running.
  public Route get(String method, String uri) {
    
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
}
