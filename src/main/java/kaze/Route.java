package kaze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.App.Func;

class Route {
  private Path root;
  private List<Path> paths = new ArrayList<>();
  private String[] split(String path) {
    String p = path.substring(1); // "/1/2" -> "1/2"
    return p.split("/"); // "1/2" -> {"1", "2"}
  }
  private boolean isRoot(String path) {
    return ("/".equals(path) || "".equals(path));
  }
  private Path find(String[] parts, boolean checking) {
    for (Path p: paths) {
      if (p.match(parts, checking)) {
        return p;
      }
    }
    return null;
  }
  //-> add
  void add(String path, Func func) {
    if (isRoot(path)) {
      if (root == null) {
        root = new Path(path, null, func);
      } else {
        duplicated(path, root);
      }
    }
    else { //-> not root path
      String[] parts = split(path);
      Path added = find(parts, true);
      if (added == null) {
        paths.add(
          new Path(path, parts, func)
        );
      } else {
        duplicated(path, added);
      }
    }
  }
  private void duplicated(String path, Path added) {
    throw new IllegalStateException(
      "Path duplicated [add=" + path + "] " +
      "[added=" + added.path + "]."
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
      p = root;
    } else {
      parts = split(path);
      p = find(parts, false);
    }
    if (p == null) return false;
    ///-> run
    Req rq = new Req(req, app, parts, p);
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
}