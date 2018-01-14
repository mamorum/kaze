package kaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {
  final Map<String, Func> staticUri = new HashMap<>();
  final List<Pattern> patternUri = new ArrayList<>();
  public void add(String path, Func f) {
    if (path.indexOf(':') == -1) staticUri.put(path, f);  // ex. /hello
    else patternUri.add(Pattern.of(path, f));  // ex. /emp/:id -> /emp/12, /emp/13
  }
  // TODO find と言いながら Req に色々と設定しているのをなんとかしたい。
  public Func find(String uri, Req req) {
    //-> dynamic url
    if (uri.length() < 2) return null; // uri -> "" or "/"
    req.pathTree = Pattern.tree(uri, 1);
    Pattern path = find(req.pathTree);
    if (path == null) return null;
    req.pathIndex = path.index;
    return path.func;
  }
  Pattern find(String[] ptree) {
    for (Pattern p: patternUri) {
      if (p.match(ptree)) return p;
    }
    return null;
  }

  static class Pattern {
    public String[] tree;
    public Map<String, Integer> index;  // for path param
    public Func func;

    //-> for register (+analyze path param)
    public static Pattern of(String path, Func func) {
      Pattern p = new Pattern();
      p.tree = tree(path, 1);
      p.index = new HashMap<>();
      for (int i=0; i<p.tree.length; i++) {
        if (p.tree[i].startsWith(":")) {
          p.index.put(p.tree[i], i);
        }
      }
      p.func = func;
      return p;
    }

    //-> common
    public static String[] tree(String path, int ltrimLen) {
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
}
