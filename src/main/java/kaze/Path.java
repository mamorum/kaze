package kaze;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Path {
  public String[] tree;
  public Map<String, Integer> index;  // for path param

  private static Path tree(String path, int ltrimLen) {
    Path p = new Path();
    p.tree = path.substring(ltrimLen).split("/");
    return p;
  }
  //-> for register (+analyze path param)
  public static Path of(String path) {
    Path p = tree(path, 1);
    p.index = new HashMap<>();
    for (int i=0; i<p.tree.length; i++) {
      if (p.tree[i].startsWith(":")) {
        p.index.put(p.tree[i], i);
      }
    }
    return p;
  }
  //-> for request
  public static Path of(HttpServletRequest req) {
    String uri = req.getRequestURI();
    String ctx = req.getContextPath();
    if (ctx == null) return tree(uri, 1);
    return tree(uri, (ctx.length() + 1));
  }
}
