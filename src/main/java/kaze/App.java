package kaze;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
  final Map<String, List<Route>> method2routes = new HashMap<>();

  //-> for app init
  private void add(String method, String uri, Func f) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) {
      routes = new ArrayList<>();
      method2routes.put(method, routes);
    }
    Path path = Path.of(uri);
    routes.add(new Route(f, path));
  }
  public void get(String uri, Func f) { add("GET", uri, f); }
  public void post(String uri, Func f) { add("POST", uri, f); }
  public void head(String uri, Func f) { add("HEAD", uri, f); }
  public void put(String uri, Func f) { add("PUT", uri, f); }
  public void options(String uri, Func f) { add("OPTIONS", uri, f); }
  public void delete(String uri, Func f) { add("DELETE", uri, f); }
  public void trace(String uri, Func f) { add("TRACE", uri, f); }
  public void connect(String uri, Func f) { add("CONNECT", uri, f); }
  public void move(String uri, Func f) { add("MOVE", uri, f); }
  public void proxy(String uri, Func f) { add("PROXY", uri, f); }
  public void pri(String uri, Func f) { add("PRI", uri, f); }
  //<- http methods are in "org.eclipse.jetty.http.HttpMethod"

  //-> for app runtime
  public boolean run(
    HttpServletRequest sreq, HttpServletResponse sres
  ) throws Exception {
    Path path = Path.of(sreq.getRequestURI());
    Route route = find(sreq.getMethod(), path);
    if (route == null) return false;  // not found
    Req req = new Req(sreq, path, route);
    Res res = new Res(sres);
    encoding(sreq, sres);
    // TODO before func
    route.func.accept(req, res);
    // TODO after func
    return true;
  }
  public Route find(String method, Path path) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) return null;
    for (Route r: routes) {
      if (match(r.path, path)) return r;
    }
    return null;
  }
  private boolean match(Path a, Path r) { // a: added, r: request
    if (a.tree.length != r.tree.length) return false;
    for (int i=0; i<a.tree.length; i++) {
      if (a.tree[i].startsWith(":")) continue;
      if (a.tree[i].equals(r.tree[i])) continue;
      return false;
    }
    return true;
  }
  ////-> encoding
  private static final String utf8 = "utf-8";
  private String enc = utf8;
  private void encoding(
    HttpServletRequest req, HttpServletResponse res)
  throws UnsupportedEncodingException
  {
    if (enc == null) return;
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(enc);
    }
    res.setCharacterEncoding(enc);
  }
  public void encoding(String enc) { this.enc=enc; }
}
