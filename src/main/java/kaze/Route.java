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
    this.path = Path.get(path);
    this.paths = Path.split(this.path);
    this.func = func;
    index();
  }
  private void index() {
    if (!path.contains(":")) return;
    index = new HashMap<>();
    for (int i=0; i<paths.length; i++) {
      if (paths[i].startsWith(":")) {
        index.put(paths[i], i);
      }
    }
  }
  boolean match(String[] tPaths, boolean checking) {
    if (paths.length != tPaths.length) return false;
    for (int i=0; i<paths.length; i++) {
      if (paths[i].startsWith(":")) continue;
      if (paths[i].equals(tPaths[i])) continue;
      if (checking) {
        if (tPaths[i].startsWith(":")) continue;
      }
      return false;
    }
    return true;
  }
}
