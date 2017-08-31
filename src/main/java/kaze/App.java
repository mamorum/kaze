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
  ////-> add (for init)
  public static void get(String path, Func f) { add(path, f, get); }
  public static void post(String path, Func f) { add(path, f, post); }
  public static void put(String path, Func f) { add(path, f, put); }
  public static void delete(String path, Func f) { add(path, f, delete); }
  ////-> exec (for runtime)
  public static boolean doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(get, req, res); }
  public static boolean doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(post, req, res); }
  public static boolean doPut(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(put, req, res); }
  public static boolean doDelete(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(delete, req, res); }
  //////-> private
  private static final List<Path>
    get=new ArrayList<>(), post=new ArrayList<>(),
    put=new ArrayList<>(), delete=new ArrayList<>();
  private static void add(String p, Func f, List<Path> paths) {
    paths.add(Path.of(p, f));
  }
  private static boolean exec(
    List<Path> paths, HttpServletRequest sreq, HttpServletResponse sres)
  throws ServletException, IOException
  {
    if (paths.isEmpty()) return false;
    String[] ptree = Path.tree(sreq);
    Path path = find(ptree, paths);
    if (path == null) return false;
    encoding(sreq, sres);
    Req req = new Req(sreq, ptree, path);
    Res res = new Res(sres);
    try { path.func.exec(req, res); }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private static Path find(String[] ptree, List<Path> from) {
    for (Path p: from) { if (p.match(ptree)) return p; }
    return null;
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

  //-> json
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