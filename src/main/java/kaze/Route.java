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
  List<Path> staticUri = new ArrayList<>();
  List<Path> dynamicUri = new ArrayList<>();
  String[] tree(String uri) {
    return uri.substring(1).split("/");
  }
  //-> add function
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
  //-> run function
  public boolean run(
    HttpServletRequest req, HttpServletResponse res, Conf conf
  ) throws ServletException, IOException {
    String uri = uri(req);
    String[] tree = null;
    Path path = findStatic(uri);
    if (path == null) {
      if (isRoot(uri)) return false;
      tree = tree(uri);
      path = findDynamic(tree);
      if (path == null) return false;
    }
    Req i = Req.from(req, uri, tree, path.index, conf);
    Res o = Res.from(res, conf);
    try { path.func.exec(i, o);  }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
  private String uri(HttpServletRequest i) {
    String c = i.getContextPath(); //-> /ctx
    String s = i.getServletPath(); //-> /srv
    String u = i.getRequestURI(); //-> /ctx/srv/one/two
    String uri = u.substring(c.length() + s.length()); //-> /one/two
    if (uri.length() == 0) return "/"; //-> convert "" to "/"
    else return uri;
  }
  private boolean isRoot(String uri) {
    return (uri.length() < 2); //-> uri is "/"
  }
  private Path findStatic(String uri) {
    for (Path p: staticUri) {
      if (uri.equals(p.uri)) return p;
    }
    return null;
  }
  public Path findDynamic(String[] tree) {
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
