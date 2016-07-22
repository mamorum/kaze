package kaze.fw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// not thread safe. 
public class Routes {

  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
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
    StringBuilder sb = new StringBuilder();
    String[] path = uri.substring(1).split("/");
    for (int i = 0; i < path.length; i++) {
      sb.append("/");
      if (path[i].contains(":")) {
        sb.append("[^/]+");
        index.put(path[i], i);
      } else {
        sb.append(path[i]);
      }
    }
    if (uri.endsWith("/")) sb.append("/");

    String regexUri = sb.toString();
    addRegexUri(method, regexUri, index, func);
    
    if (logger.isDebugEnabled()) {
      String uri4log = regexUri.replaceAll(
          "\\[\\^/\\]\\+", "*"
      );
      logRoute(method, uri4log, func);
    }
  }
  
  private void addRegexUri(
      String method, String regexUri,
      Map<String, Integer> uriIndex, Func func
  ) {
    ArrayList<Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new ArrayList<>();
      method2regex.put(method, regexRoutes);
    }
    Pattern pattern = Pattern.compile(regexUri);
    regexRoutes.add(
        new Route(func, pattern, uriIndex)
    );    
  }

  private void addUri(String method, String uri, Func func) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    uriRoutes.put(uri, new Route(func));    
    logRoute(method, uri, func);
  }

  private void logRoute(String method, String uri, Func func) {
    logger.debug(
        "[{} {}] -> [{}#{}]", method, uri, 
        func.m.getDeclaringClass().getName(), 
        func.m.getName());
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
