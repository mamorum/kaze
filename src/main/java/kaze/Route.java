package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Route {
  //-> common
  private Path rootPath;
  private List<Path> staticPath = new ArrayList<>();
  private List<Path> dynamicPath = new ArrayList<>();
  private String[] split(String appPath) {
    String p = appPath.substring(1);
    return p.split("/"); // "/p1/p2" -> {"p1", "p2"}
  }
  private boolean isRoot(String path) {
    return ("/".equals(path) || "".equals(path));
  }
  //-> add
  public void add(String path, Func func) {
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
  public boolean run(
    HttpServletRequest req, HttpServletResponse res, Conf conf
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
    encoding(req, res, conf.encoding);
    Req rq = new Req(req, path, parts, p.index, conf);
    Res rs = new Res(res, conf);
    try { p.func.exec(rq, rs);  }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private String appPath(HttpServletRequest req) {
    String c = req.getContextPath(); //-> /ctxt
    String s = req.getServletPath(); //-> /srvlt
    String u = req.getRequestURI(); //-> /ctxt/srvlt/path1/path2
    String path = u.substring( //-> /path1/path2
      c.length() + s.length()
    );
    return path;
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
  private void encoding(
    HttpServletRequest req, HttpServletResponse res, String enc)
  throws UnsupportedEncodingException {
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(enc);
    }
    res.setCharacterEncoding(enc);
  }
}