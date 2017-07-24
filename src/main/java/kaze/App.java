package kaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
  //-> routing (Methods are in "org.eclipse.jetty.http.HttpMethod")
  public void get(String path, Func f) { add("GET", path, f); }
  public void post(String path, Func f) { add("POST", path, f); }
  public void head(String path, Func f) { add("HEAD", path, f); }
  public void put(String path, Func f) { add("PUT", path, f); }
  public void options(String path, Func f) { add("OPTIONS", path, f); }
  public void delete(String path, Func f) { add("DELETE", path, f); }
  public void trace(String path, Func f) { add("TRACE", path, f); }
  public void connect(String path, Func f) { add("CONNECT", path, f); }
  public void move(String path, Func f) { add("MOVE", path, f); }
  public void proxy(String path, Func f) { add("PROXY", path, f); }
  public void pri(String path, Func f) { add("PRI", path, f); }

  public void add(String method, String path, Func f) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) {
      routes = new ArrayList<>();
      method2routes.put(method, routes);
    }
    Route route = new Route();
    route.func = f;
    route.path = path;
    route.paths = path.substring(1).split("/");
    route.param = path.contains(":");
    routes.add(route);
  }

  public final Map<String, List<Route>>
    method2routes = new HashMap<>();

  public Route find(Req req) {
    List<Route> routes = method2routes.get(req.method);
    if (routes == null) return null;
    for (Route route: routes) {
      if (match(route, req)) return route;
    }
    return null;
  }
  private boolean match(Route route, Req req) {
    if (route.param) {
      req.paths = req.path.substring(1).split("/");
      req.pathParam = new HashMap<>();
      String[] added = route.paths;
      String[] actual = req.paths;
      if (added.length != actual.length) return false;
      for (int i=0; i<added.length; i++) {
        if (added[i].startsWith(":")) {
          req.pathParam.put(added[i], actual[i]);
          continue;
        }
        if (added[i].equals(actual[i])) continue;
        else return false;
      }
      return true;
    }
    return route.path.equals(req.path);
  }

  @FunctionalInterface public interface Func {
    void accept(Req req, Res res) throws Throwable;
  }
  public class Route {
    public Func func;
    public String path;
    public String[] paths;
    public boolean param;
  }
}
