package kaze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.App.Func;

class Route {
  private Path rootPath;
  private List<Path> staticPath = new ArrayList<>();
  private List<Path> dynamicPath = new ArrayList<>();
  private class Path {
    String path;
    String[] parts;
    Map<String, Integer> index;
    Func func;
    Path(String path, Func func) {
      this.path=path; this.func=func;
    }
  }
  private String[] split(String path) {
    String p = path.substring(1); // "/1/2" -> "1/2"
    return p.split("/"); // "1/2" -> {"1", "2"}
  }
  private boolean isRoot(String path) {
    return ("/".equals(path) || "".equals(path));
  }
  //-> add
  void add(String path, Func func) {
    // TODO path の重複チェック
    if (isRoot(path)) {
      rootPath = new Path(path, func);
    } else {
      //-> static ( path has no ':' )
      if (path.indexOf(':') == -1) {
        staticPath.add(new Path(path, func));
      } else { //-> dynamic ( path has ':' )
        Path p = new Path(path, func);
        dynamicPath.add(analyze(p));
      }
    }
  }
  private Path analyze(Path p) {
    p.parts = split(p.path);
    p.index = new HashMap<>();
    for (int i=0; i<p.parts.length; i++) {
      if (p.parts[i].startsWith(":")) {
        p.index.put(p.parts[i], i);
      }
    }
    return p;
  }
  //-> run
  boolean run(
    HttpServletRequest req, HttpServletResponse res, App app
  ) throws ServletException, IOException {
    ////-> find
    Path p; // target
    String[] parts = null;
    String path = appPath(req);
    if (isRoot(path)) {
      p = rootPath;
    } else {
      p = findStatic(path);
      if (p == null) {
        parts = split(path);
        p = findDynamic(parts);
      }
    }
    if (p == null) return false;
    ////-> run
    Req rq = new Req(req, path, parts, p.index, app);
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
  private Path findStatic(String path) {
    for (Path p: staticPath) {
      if (path.equals(p.path)) return p;
    }
    return null;
  }
  private Path findDynamic(String[] parts) {
    for (Path p: dynamicPath) {
      if (p.parts.length != parts.length) return null;
      for (int i=0; i<p.parts.length; i++) {
        if (p.parts[i].startsWith(":")) continue;
        if (p.parts[i].equals(parts[i])) continue;
        return null;
      }
      return p;
    }
    return null;
  }
}