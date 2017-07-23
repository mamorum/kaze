package kaze;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import kaze.App.Func;

public class Routes {
  private final HashMap<String, HashMap<String, Route>>
    method2uri = new HashMap<>();
  private final HashMap<String, HashMap<Pattern, Route>>
    method2regex = new HashMap<>();

  //-> add
  public void add(String method, String path, Func f) {
    if (path.contains(":")) addRegex(method, path, f);
    else addUri(method, path, f);
  }
  private void addRegex(String method, String path, Func func) {
    HashMap<Pattern, Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new HashMap<>();
      method2regex.put(method, regexRoutes);
    }
    // Create URI index and,
    // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
    Map<String, Integer> pathIndex = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    String[] paths = path.substring(1).split("/");
    for (int i = 0; i < paths.length; i++) {
      sb.append("/");
      if (paths[i].contains(":")) {
        sb.append("[^/]+");
        pathIndex.put(paths[i], i);
      } else {
        sb.append(paths[i]);
      }
    }
    if (path.endsWith("/")) sb.append("/");
    Route route = new Route(func, pathIndex);
    Pattern regex = Pattern.compile(sb.toString());
    regexRoutes.put(regex, route);
  }
  private void addUri(String method, String path, Func func) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    Route route = new Route(func, null);
    uriRoutes.put(path, route);
  }

  //-> get
  public Route plainUriRoute(String method, String path) {
    HashMap<String, Route> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) return null;
    return uriRoutes.get(path);
  }
  public Route regexUriRoute(String method, String path) {
    HashMap<Pattern, Route> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) return null;
    for(Pattern r : regexRoutes.keySet()) {
      if (r.matcher(path).matches()) return regexRoutes.get(r);
    }
    return null;  // not found
  }
}
