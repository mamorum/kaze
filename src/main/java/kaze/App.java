package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
  //-> routing
  private final Map<String, Func>
    getUri=new HashMap<>(), postUri=new HashMap<>(),
    putUri=new HashMap<>(), deleteUri=new HashMap<>();
  private final List<Route>
    getRoute=new ArrayList<>(), postRoute=new ArrayList<>(),
    putRoute=new ArrayList<>(), deleteRoute=new ArrayList<>();
  ////-> add
  public void get(String path, Func f) { add(path, f, getUri, getRoute); }
  public void post(String path, Func f) { add(path, f, postUri, postRoute); }
  public void put(String path, Func f) { add(path, f, putUri, putRoute); }
  public void delete(String path, Func f) { add(path, f, deleteUri, deleteRoute); }
  private void add(String path, Func f, Map<String, Func> uri, List<Route> route) {
    if (path.indexOf(':') == -1) uri.put(path, f);  // ex. /hello
    else route.add(new Route(Path.of(path), f));  // dynamic uri -> /emp/12, /emp/13
  }
  ////-> run
  String path(HttpServletRequest req) {
    String c = req.getContextPath();
    String s = req.getServletPath();
    String u = req.getRequestURI();
    String path = u.substring(
      c.length() + s.length()
    );
    System.out.println(
      "ContextPath=" + c +
      ", ServletPath=" + s +
      ", RequestURI=" + u +
      ", Path=" + path
    );
    return path;
    // return (requestUri - (contextPath + servletPath))
    //// ex.
    ////  requestUri: /context/servlet/a/b
    ////  contextPath: /context
    ////  servletPath: /servlet
    ////  path: /a/b
  }
  public boolean runGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException { return exec(getUri, getRoute, req, res); }
  public boolean runPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException { return exec(postUri, postRoute, req, res); }
  public boolean runPut(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException { return exec(putUri, putRoute, req, res); }
  public boolean runDelete(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException { return exec(deleteUri, deleteRoute, req, res); }
  private boolean exec(
    Map<String, Func> uri, List<Route> routes,
    HttpServletRequest sreq, HttpServletResponse sres)
  throws ServletException, IOException {
    String path = path(sreq);
    Func func = uri.get(path);
    if (func != null) return exec(sreq, sres, func, null, null);
    //-> check dynamic url
    if (path.length() < 2) return false;  // blank path "" & root path "/" -> return false
    System.out.println("URI: dynamic");
    String[] ptree = Path.tree(path, 1);
    Route route = find(ptree, routes);
    if (route == null) return false;
    return exec(sreq, sres, route.func, ptree, route.path.index);
  }
  public boolean exec(
    HttpServletRequest sreq, HttpServletResponse sres, Func func,
    String[] pathTree, Map<String, Integer> pathIndex
  ) throws ServletException, IOException {
    Req req = new Req(sreq, json2obj, pathTree, pathIndex);
    Res res = new Res(sres, obj2json);
    encoding(req, res);
    try { func.exec(req, res); }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private Route find(String[] ptree, List<Route> from) {
    for (Route r: from) {
      if (r.path.match(ptree)) return r;
    }
    return null;
  }
  //-> servlet
  public Servlet servlet() {
    Servlet s = new Servlet();
    s.app = this;
    return s;
  }
  @SuppressWarnings("serial")
  public static class Servlet extends HttpServlet {
    protected App app;
    @Override protected void doGet(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      if (!app.runGet(req, res)) res.sendError(404);
    }
    @Override protected void doPost(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException{
      if (!app.runPost(req, res)) res.sendError(404);
    }
    @Override protected void doPut(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      if (!app.runPut(req, res)) res.sendError(404);
    }
    @Override protected void doDelete(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      if (!app.runDelete(req, res)) res.sendError(404);
    }
  }
  //-> encoding
  public String encoding = "utf-8";
  private void encoding(Req req, Res res) throws UnsupportedEncodingException {
    if (encoding == null) return;
    if (req.srv.getCharacterEncoding() == null) {
      req.srv.setCharacterEncoding(encoding);
    }
    res.srv.setCharacterEncoding(encoding);
  }
  //-> json
  Json2obj json2obj = App::noJson2obj;
  Obj2json obj2json = App::noObj2json;
  public void parser(Json2obj j2o, Obj2json o2j) {
    json2obj=j2o;  obj2json=o2j;
  }
  @FunctionalInterface public static interface Json2obj {
    <T> T exec(String json, Class<T> obj);
  }
  @FunctionalInterface public static interface Obj2json {
    String exec(Object obj);
  }
  static String errMsg =
    "No json parser found. Call `App#parser(Json2obj, Obj2json)` to set.";
  static <T> T noJson2obj(String json, Class<T> obj) {
    throw new IllegalStateException(errMsg);
  }
  static String noObj2json(Object obj) {
    throw new IllegalStateException(errMsg);
  }
}