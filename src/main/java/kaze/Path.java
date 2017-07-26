package kaze;

import java.util.HashMap;
import java.util.Map;

public class Path {
  public String uri;
  public String[] tree;
  public Map<String, Integer> index;

  public static Path of(String uri) {
    Path p = new Path();
    p.uri = uri;
    p.tree = uri.substring(1).split("/");
    p.index = new HashMap<>();
    for (int i=0; i<p.tree.length; i++) {
      if (p.tree[i].startsWith(":")) {
        p.index.put(p.tree[i], i);
      }
    }
    return p;
  }
}
