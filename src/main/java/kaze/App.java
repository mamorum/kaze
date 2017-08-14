package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
  //-> routing
  private static final List<Route>
    get=new ArrayList<>(), post=new ArrayList<>(),
    put=new ArrayList<>(), delete=new ArrayList<>();
  ////-> for init (add route)
  private static void add(List<Route> routes, String path, Func f) {
    Path p = Path.of(path);
    routes.add(new Route(p, f));
  }
  public static void get(String path, Func f) { add(get, path, f); }
  public static void post(String path, Func f) { add(post, path, f); }
  public static void put(String path, Func f) { add(put, path, f); }
  public static void delete(String path, Func f) { add(delete, path, f); }
  ////-> for runtime (exec function)
  public static boolean doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return run(get, req, res); }
  public static boolean doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return run(post, req, res); }
  public static boolean doPut(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return run(put, req, res); }
  public static boolean doDelete(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return run(delete, req, res); }
  private static boolean run(List<Route> routes,
    HttpServletRequest sreq, HttpServletResponse sres
  ) throws ServletException, IOException {
    if (routes.isEmpty()) return false;
    Path path = Path.of(sreq);
    Route route = find(path, routes);
    if (route == null) return false;
    Req req = new Req(sreq, path, route);
    Res res = new Res(sres);
    encoding(sreq, sres);
    try { route.func.exec(req, res); }
    catch (Exception e) {
      throw new ServletException(e);
    }
    return true;
  }
  private static Route find(Path reqPath, List<Route> from) {
    for (Route r: from) {
      if (match(r.path, reqPath)) return r;
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

  //-> encoding
  public static String encoding = "utf-8";
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

  //-> json parser
  public static void parser(Json2obj j2o, Obj2json o2j) {
    json2obj=j2o;  obj2json=o2j;
  }
  static Json2obj json2obj;
  static Obj2json obj2json;
  @FunctionalInterface public static interface Json2obj {
    <T> T exec(String json, Class<T> obj);
  }
  @FunctionalInterface public static interface Obj2json {
    String exec(Object obj);
  }

  //-> servlet
  @SuppressWarnings("serial")
  public static class Servlet extends HttpServlet {
    @Override protected void doGet(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doGet(req, res);
      if (!done) res.sendError(404);
    }
    @Override protected void doPost(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doPost(req, res);
      if (!done) res.sendError(404);
    }
    @Override protected void doPut(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doPut(req, res);
      if (!done) res.sendError(404);
    }
    @Override protected void doDelete(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean done = App.doDelete(req, res);
      if (!done) res.sendError(404);
    }
  }
}