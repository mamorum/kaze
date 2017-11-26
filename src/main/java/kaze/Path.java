package kaze;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Path {
  public String[] tree;
  public Map<String, Integer> index;  // for path param
  public Func func;

  //-> for register (+analyze path param)
  public static Path of(String path, Func f) {
    Path p = new Path();
    p.tree = tree(path, 1);
    p.index = new HashMap<>();
    for (int i=0; i<p.tree.length; i++) {
      if (p.tree[i].startsWith(":")) {
        p.index.put(p.tree[i], i);
      }
    }
    p.func = f;
    return p;
  }

  //-> for request
  public static String[] tree(HttpServletRequest req) {
    String c = req.getContextPath();
    String s = req.getServletPath();
    String uri = req.getRequestURI();
    int prefix = c.length() + s.length();
    if (uri.length() == prefix) return tree(uri, prefix);
    return tree(uri, (prefix + 1));
  }

  //-> common
  private static String[] tree(String path, int ltrimLen) {
    return path.substring(ltrimLen).split("/");
  }
  public boolean match(String[] ptree) {
    if (this.tree.length != ptree.length) return false;
    for (int i=0; i<this.tree.length; i++) {
      if (this.tree[i].startsWith(":")) continue;
      if (this.tree[i].equals(ptree[i])) continue;
      return false;
    }
    return true;
  }
}