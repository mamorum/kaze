package kaze.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Routes {

//  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
  private static HashMap<String, HashMap<String, Route.Plain>>
    method2uri = new HashMap<>();
  
  private static HashMap<String, ArrayList<Route.Regex>>
    method2regex = new HashMap<>();
  
  public static void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegex(method, uri, func);
    else addUri(method, uri, func);
  }
  
  private static void addRegex(String method, String uri, Func func) {
    ArrayList<Route.Regex> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new ArrayList<>();
      method2regex.put(method, regexRoutes);
    }
    Route.Regex route = regexRoute(method, uri, func);
    regexRoutes.add(route);
  }
  
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
  private static Route.Regex regexRoute(String method, String uri, Func func) {
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
    return new Route.Regex(regexUri, uriIndex, func);
  }

  private static void addUri(String method, String uri, Func func) {
    HashMap<String, Route.Plain> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    Route.Plain route = uriRoute(method, uri, func);
    uriRoutes.put(uri, route);
  }

  private static Route.Plain uriRoute(String method, String uri, Func func) {
    return new Route.Plain(func);
  }

  // for runtime.
  public static Route plainUriRoute(String method, String uri) {    
    HashMap<String, Route.Plain> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) return null;
    return uriRoutes.get(uri);
  }  
  public static Route regexUriRoute(String method, String uri) {  
    ArrayList<Route.Regex> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) return null;
    for(Route.Regex r : regexRoutes) {
      if (r.uriPattern.matcher(uri).matches()) return r;
    }
    return null;  // not found
  }
}
