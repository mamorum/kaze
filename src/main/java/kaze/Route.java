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
  List<Path> staticUri = new ArrayList<>();
  List<Path> dynamicUri = new ArrayList<>();
  public void add(String path, Func f) {
    if (path.indexOf(':') == -1) staticUri.add(new Path(path, f));  // ex. /hello
    else dynamicUri.add(path(path, f));  // ex. /emp/:id -> /emp/12, /emp/13
  }
  Path path(String path, Func func) {
    String[] tree = tree(path);
    Map<String, Integer> index = new HashMap<>();
    for (int i=0; i<tree.length; i++) {
      if (tree[i].startsWith(":")) index.put(tree[i], i);
    }
    return new Path(tree, index, func);
  }
  public String[] tree(String uri) {
    if (uri.length() < 2) { // uri -> "" or "/"
      return null;
    }
    return uri.substring(1).split("/");
  }
  private static String uri(HttpServletRequest i) {
    String c = i.getContextPath(); //-> /ctx
    String s = i.getServletPath(); //-> /srv
    String u = i.getRequestURI(); //-> /ctx/srv/one/two
    return u.substring(c.length() + s.length()); //-> /one/two
  }
  public boolean run(
    HttpServletRequest req, HttpServletResponse res, Conf conf
  ) throws ServletException, IOException {
    String uri = uri(req);
    Path path = find(uri);
    if (path != null) {
      Req r = Req.from(req, uri, null, null, conf);
      return run(path.func, r, res, conf);
    }
    String[] tree = tree(uri);
    path = find(tree);
    if (path != null) {
      Req r = Req.from(req, uri, tree, path.index, conf);
      return run(path.func, r, res, conf);
    }
    return false;
  }
  boolean run(Func f, Req req, HttpServletResponse o, Conf c)
    throws ServletException, IOException {
    Res res = Res.from(o, c);
    try { f.exec(req, res);  }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private Path find(String uri) {
    for (Path p: staticUri) {
      if (uri.equals(p.uri)) return p;
    }
    return null;
  }
  public Path find(String[] tree) {
    if (tree == null) return null; // uri -> "" or "/"
    for (Path p: dynamicUri) {
      if (p.tree.length != tree.length) return null;
      for (int i=0; i<p.tree.length; i++) {
        if (p.tree[i].startsWith(":")) continue;
        if (p.tree[i].equals(tree[i])) continue;
        return null;
      }
      return p;
    }
    return null;
  }
}
