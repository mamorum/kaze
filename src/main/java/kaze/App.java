package kaze;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.Route.Func;

public class App {
  final Map<String, List<Route>> method2routes = new HashMap<>();

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

  public boolean run(
    HttpServletRequest sreq, HttpServletResponse sres
  ) throws Exception {
    String path = sreq.getRequestURI();
    String[] paths = path.substring(1).split("/");
    Route route = find(sreq.getMethod(), path, paths);
    if (route == null) return false;  // not found
    Req req = new Req(sreq, paths, route);
    Res res = new Res(sres);
    utf8(sreq, sres);
    route.func.accept(req, res);
    return true;
  }
  public Route find(String method, String path, String[] paths) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) return null;
    for (Route route: routes) {
      if (match(route, path, paths)) return route;
    }
    return null;
  }
  private boolean match(Route rt, String path, String[] paths) {
    if (rt.params == null) return rt.path.equals(path);
    if (rt.paths.length != paths.length) return false;
    for (int i=0; i<rt.paths.length; i++) {
      if (rt.paths[i].startsWith(":")) continue;
      if (rt.paths[i].equals(paths[i])) continue;
      else return false;
    }
    return true;
  }

  private static final String utf8 = "utf-8";
  private void utf8(
    HttpServletRequest req, HttpServletResponse res)
    throws UnsupportedEncodingException
  {
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(utf8);
    }
    res.setCharacterEncoding(utf8);
  }
}
