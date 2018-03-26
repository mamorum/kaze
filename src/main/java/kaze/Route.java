package kaze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.App.Func;

class Route {
  private String method;
  private Path rootPath;
  private List<Path> paths = new ArrayList<>();
  public Route(String method) {this.method=method;}
  private String[] split(String path) {
    String p = path.substring(1); // "/1/2" -> "1/2"
    return p.split("/"); // "1/2" -> {"1", "2"}
  }
  private boolean isRoot(String path) {
    return ("/".equals(path) || "".equals(path));
  }
  private boolean match( // is match both path parts ?
    String[] add, String[] added, boolean checking
  ) {
    if (add.length != added.length) return false;
    for (int i=0; i<added.length; i++) {
      if (added[i].startsWith(":")) continue;
      if (added[i].equals(add[i])) continue;
      if (checking) {
        if (add[i].startsWith(":")) continue;
      }
      return false;
    }
    return true;
  }
  //-> add
  void add(String path, Func func) {
    if (isRoot(path)) {
      if (rootPath != null) duplicated(path);
      else rootPath = new Path(path, null, func);
      return;
    }
    String[] parts = split(path);
    if (exists(parts)) duplicated(path);
    paths.add(new Path(path, parts, func));
  }
  private boolean exists(String[] parts) {
    for (Path p: paths) {
      if (match(parts, p.parts, true)) return true;
    }
    return false;
  }
  private void duplicated(String path) {
    throw new IllegalStateException(
      "Path already exists. " +
      "[path=" + path + "] " +
      "[method=" + method + "] "
    );
  }
  //-> run
  boolean run(
    HttpServletRequest req, HttpServletResponse res, App app
  ) throws ServletException, IOException {
    ///-> find
    Path p; // target
    String path = appPath(req);
    String[] parts = null; // splited path
    if (isRoot(path)) {
      p = rootPath;
    } else {
      parts = split(path);
      p = find(parts);
    }
    if (p == null) return false;
    ///-> run
    Req rq = new Req(req, app, p.index, parts);
    Res rs = new Res(res, app);
    try { p.func.exec(rq, rs);  }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private String appPath(HttpServletRequest req) {
    String c = req.getContextPath(); //-> /ctxt
    String s = req.getServletPath(); //-> /srvlt
    String r = req.getRequestURI(); //-> /ctxt/srvlt/1/2
    String appPath = r.substring( //-> /1/2
      c.length() + s.length()
    );
    return appPath;
  }
  private Path find(String[] parts) {
    for (Path p: paths) {
      if (match(parts, p.parts, false)) return p;
    }
    return null;
  }
}