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
    String[] paths = path.substring(1).split("/");
    Map<String, Integer> params = null;
    if (path.contains(":")) {
      params = new HashMap<>();
      for (int i=0; i<paths.length; i++) {
        if (paths[i].startsWith(":")) {
          params.put(paths[i], i);
        }
      }
    }
    routes.add(
      new Route(f, path, paths, params)
    );
  }

  public final Map<String, List<Route>>
    method2routes = new HashMap<>();

  public Route find(String method, Req req) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) return null;
    for (Route route: routes) {
      if (match(route, req)) return route;
    }
    return null;
  }
  private boolean match(Route rt, Req rq) {
    if (rt.params == null) return rt.path.equals(rq.path);
    if (rt.paths.length != rq.paths.length) return false;
    for (int i=0; i<rt.paths.length; i++) {
      if (rt.paths[i].startsWith(":")) continue;
      if (rt.paths[i].equals(rq.paths[i])) continue;
      else return false;
    }
    return true;
  }

  @FunctionalInterface public interface Func {
    void accept(Req req, Res res) throws Throwable;
  }
  public class Route {
    public Func func;
    public String path;
    public String[] paths;
    public Map<String, Integer> params;
    public Route(
      Func func, String path,String[] paths,
      Map<String, Integer> params
    ) {
      this.func=func; this.path=path;
      this.paths=paths; this.params=params;
    }
  }
}
