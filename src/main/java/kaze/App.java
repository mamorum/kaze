package kaze;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
  static final Map<String, List<Route>> method2routes = new HashMap<>();

  //-> for app init
  ////-> routing (http methods are in "org.eclipse.jetty.http.HttpMethod")
  public static void get(String uri, Func f) { add("GET", uri, f); }
  public static void post(String uri, Func f) { add("POST", uri, f); }
  public static void head(String uri, Func f) { add("HEAD", uri, f); }
  public static void put(String uri, Func f) { add("PUT", uri, f); }
  public static void options(String uri, Func f) { add("OPTIONS", uri, f); }
  public static void delete(String uri, Func f) { add("DELETE", uri, f); }
  public static void trace(String uri, Func f) { add("TRACE", uri, f); }
  public static void connect(String uri, Func f) { add("CONNECT", uri, f); }
  public static void move(String uri, Func f) { add("MOVE", uri, f); }
  public static void proxy(String uri, Func f) { add("PROXY", uri, f); }
  public static void pri(String uri, Func f) { add("PRI", uri, f); }
  private static void add(String method, String uri, Func f) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) {
      routes = new ArrayList<>();
      method2routes.put(method, routes);
    }
    Path path = Path.of(uri);
    routes.add(new Route(f, path));
  }
  ////-> functions
  @FunctionalInterface public static interface Func {
    void exec(Req req, Res res) throws Exception;
  }
  @FunctionalInterface public static interface FromJson {
    <T> T exec(String json, Class<T> to);
  }
  @FunctionalInterface public static interface ToJson {
    String exec(Object from);
  }
  ////-> json parser (functions)
  public static FromJson fromJson;
  public static ToJson toJson;
  public static void parser(FromJson json2obj, ToJson obj2json) {
    fromJson=json2obj;  toJson=obj2json;
  }

  //-> for app runtime
  public static boolean exist() {
    return (method2routes.size() > 0);
  }
  public static boolean run(
    HttpServletRequest sreq, HttpServletResponse sres
  ) throws Exception {
    Path path = Path.of(sreq.getRequestURI());
    Route route = find(sreq.getMethod(), path);
    if (route == null) return false;  // not found
    Req req = new Req(sreq, path, route);
    Res res = new Res(sres);
    encoding(sreq, sres);
    // TODO before func
    route.func.exec(req, res);
    // TODO after func
    return true;
  }
  public static Route find(String method, Path path) {
    List<Route> routes = method2routes.get(method);
    if (routes == null) return null;
    for (Route r: routes) {
      if (match(r.path, path)) return r;
    }
    return null;
  }
  private static boolean match(Path a, Path r) { // a: added, r: request
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
  private static String encoding = utf8;
  private static void encoding(
    HttpServletRequest req, HttpServletResponse res)
  throws UnsupportedEncodingException
  {
    if (encoding == null) return;
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(encoding);
    }
    res.setCharacterEncoding(encoding);
  }
  public static void encoding(String enc) { encoding=enc; }
}
