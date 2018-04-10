package kaze;

import java.util.HashMap;
import java.util.Map;

import kaze.App.Func;

class Route {
  String path;
  String[] paths;
  Func func;
  Map<String, Integer> index; //-> for path param
  //-> for runtime (to resolve route.)
  Route(String path) {
    this.path=path;
    this.paths=split(path);
  }
  //-> for init (to add route.)
  Route(String path, Func f) {
    this(path);
    this.func=f;
    createIndex();
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
    if ("".equals(path) || "/".equals(path)) {
      return root;  //-> to match "" with "/".
    }
    //-> to match "/1/2" with "/1/2/".
    /// "/1/2"->"1/2", "/1/2/"->"1/2/",
    String trimed = path.substring(1);
    /// "1/2"->{"1", "2"}, "1/2/"->{"1", "2"}
    return trimed.split("/");
  }
  boolean match(Route target, boolean checking) {
    if (paths.length != target.paths.length) return false;
    for (int i=0; i<paths.length; i++) {
      if (paths[i].startsWith(":")) continue;
      if (paths[i].equals(target.paths[i])) continue;
      if (checking) {
        if (target.paths[i].startsWith(":")) continue;
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
