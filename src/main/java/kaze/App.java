package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
  //-> routing
  private final List<Path>
    get=new ArrayList<>(), post=new ArrayList<>(),
    put=new ArrayList<>(), delete=new ArrayList<>();
  ////-> add routing (for init)
  public void get(String path, Func f) { add(path, f, get); }
  public void post(String path, Func f) { add(path, f, post); }
  public void put(String path, Func f) { add(path, f, put); }
  public void delete(String path, Func f) { add(path, f, delete); }
  private void add(String p, Func f, List<Path> paths) {
    paths.add(Path.of(p, f));
  }
  ////-> exec function (for runtime)
  public boolean doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(get, req, res); }
  public boolean doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(post, req, res); }
  public boolean doPut(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(put, req, res); }
  public boolean doDelete(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException { return exec(delete, req, res); }
  private boolean exec(
    List<Path> paths, HttpServletRequest sreq, HttpServletResponse sres)
  throws ServletException, IOException
  {
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
  public void parser(Json2obj j2o, Obj2json o2j) {
    json2obj=j2o;  obj2json=o2j;
  }
  Json2obj json2obj;
  Obj2json obj2json;
  @FunctionalInterface public static interface Json2obj {
    <T> T exec(String json, Class<T> obj);
  }
  @FunctionalInterface public static interface Obj2json {
    String exec(Object obj);
  }
}