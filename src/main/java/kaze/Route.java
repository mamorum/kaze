package kaze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Route {
  //-> common
  private List<Path> staticPath = new ArrayList<>();
  private List<Path> dynamicPath = new ArrayList<>();
  private String[] split(String appPath) {
    // "/path1/path2" -> {"path1", "path2"}
    return appPath.substring(1).split("/");
  }
  //-> add
  public void add(String appPath, Func func) {
    // TODO path の重複チェック
    if (appPath.indexOf(':') > -1) {
      String[] parts = split(appPath);
      Map<String, Integer> index = analyze(parts);
      dynamicPath.add(new Path(parts, index, func));
    } else {
      staticPath.add(new Path(appPath, func));
    }
  }
  private Map<String, Integer> analyze(String[] pathParts) {
    Map<String, Integer> index = new HashMap<>();
    for (int i=0; i<pathParts.length; i++) {
      if (pathParts[i].startsWith(":")) {
        index.put(pathParts[i], i);
      }
    }
    return index;
  }
  //-> run
  public boolean run(
    HttpServletRequest req, HttpServletResponse res, Conf conf
  ) throws ServletException, IOException {
    String[] pathParts = null;
    String appPath = appPath(req);
    Path path = findStatic(appPath);
    if (path == null) {
      if (isRoot(appPath)) return false;
      pathParts = split(appPath);
      path = findDynamic(pathParts);
      if (path == null) return false;
    }
    Req rq = Req.from(req, appPath, pathParts, path.index, conf);
    Res rs = Res.from(res, conf);
    try { path.func.exec(rq, rs);  }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private String appPath(HttpServletRequest req) {
    String c = req.getContextPath(); //-> /ctxt
    String s = req.getServletPath(); //-> /srvlt
    String u = req.getRequestURI(); //-> /ctxt/srvlt/path1/path2
    String appPath = u.substring( //-> /path1/path2
      c.length() + s.length()
    );
    if (appPath.length() == 0) {
      return "/"; // "" -> "/"
    } else {
      return appPath;
    }
  }
  private boolean isRoot(String appPath) {
    return (appPath.length() < 2); // "/" -> true
  }
  private Path findStatic(String appPath) {
    for (Path p: staticPath) {
      if (appPath.equals(p.path)) return p;
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
