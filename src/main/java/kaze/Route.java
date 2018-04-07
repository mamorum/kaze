package kaze;

import java.util.HashMap;
import java.util.Map;

import kaze.App.Func;

class Route {
  String path;
  String[] paths;
  Map<String, Integer> index;
  Func func;
  Route(String path, Func func) {
    this(path);
    createIndex();
    this.func=func;
  }
  Route(String path) {
    this.path=path;
    this.paths=split(path);
  }
  private void createIndex() {
    if (!path.contains(":")) return;
    index = new HashMap<>();
    for (int i=0; i<paths.length; i++) {
      if (paths[i].startsWith(":")) {
        index.put(paths[i], i);
      }
    }
  }
  private static final String[] root = {""};
  private String[] split(String path) {
    if ("".equals(path)) return root;
    if ("/".equals(path)) return root;
    String trimed = path.substring(1); // "/1/2" -> "1/2"
    return trimed.split("/"); // "1/2" -> {"1", "2"}
  }
  boolean match(Route t, boolean checking) {
    if (paths.length != t.paths.length) return false;
    for (int i=0; i<paths.length; i++) {
      if (paths[i].startsWith(":")) continue;
      if (paths[i].equals(t.paths[i])) continue;
      if (checking) {
        if (t.paths[i].startsWith(":")) continue;
      }
      return false;
    }
    return true;
  }
  void inherit(Route from) {
    index = from.index;
    func = from.func;
  }
}
