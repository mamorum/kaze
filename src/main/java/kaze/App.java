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
  private final List<Path>
    get=new ArrayList<>(), post=new ArrayList<>(),
    put=new ArrayList<>(), delete=new ArrayList<>();
  ////-> add (for init)
  public void get(String path, Func f) { add(path, f, get); }
  public void post(String path, Func f) { add(path, f, post); }
  public void put(String path, Func f) { add(path, f, put); }
  public void delete(String path, Func f) { add(path, f, delete); }
  private void add(String p, Func f, List<Path> paths) {
    paths.add(Path.of(p, f));
  }
  ////-> exec (for runtime)
  public boolean runGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(get, req, res); }
  public boolean runPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(post, req, res); }
  public boolean runPut(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(put, req, res); }
  public boolean runDelete(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(delete, req, res); }
  private boolean exec(
    List<Path> paths, HttpServletRequest sreq, HttpServletResponse sres)
  throws ServletException, IOException {
    if (paths.isEmpty()) return false;
    String[] ptree = Path.tree(sreq);
    Path path = find(ptree, paths);
    if (path == null) return false;
    encoding(sreq, sres);
    Req req = new Req(sreq, ptree, path, json2obj);
    Res res = new Res(sres, obj2json);
    try { path.func.exec(req, res); }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private Path find(String[] ptree, List<Path> from) {
    for (Path p: from) {
      if (p.match(ptree)) return p;
    }
    return null;
  }
  //-> servlet
  public Servlet servlet() { return new Servlet(this);}
  @SuppressWarnings("serial")
  public static class Servlet extends HttpServlet {
    protected App app;
    public Servlet() { super(); app=new App(); }
    public Servlet(App ap) { super(); app=ap; }
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
  public static String encoding = "utf-8";
  private void encoding(
    HttpServletRequest req, HttpServletResponse res)
  throws UnsupportedEncodingException
  {
    if (encoding == null) return;
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(encoding);
    }
    res.setCharacterEncoding(encoding);
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
  private static final String errMsg =
    "No json parser found. Call `App#parser(Json2obj, Obj2json)` to set.";
  static <T> T noJson2obj(String json, Class<T> obj) {
    throw new IllegalStateException(errMsg);
  }
  static String noObj2json(Object obj) {
    throw new IllegalStateException(errMsg);
  }
}