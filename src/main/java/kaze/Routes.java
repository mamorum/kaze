package kaze;

import java.util.ArrayList;
import java.util.List;

class Routes {
  private List<Route> rts = new ArrayList<>();
  private Route find(
    String path, String[] paths, boolean checking
  ) {
    for (Route rt: rts) {
      if (match(path, paths, rt, checking)) {
        return rt;
      }
    }
    return null;
  }
  void add(String path, String[] paths, Fn f) {
    Route rt = find(path, paths, true);
    if (rt == null) {
      rts.add(new Route(path, paths, f));
    } else {
      throw new IllegalStateException(
        "Path duplicated [add=" + path + "] " +
        "[added=" + rt.path + "]."
      );
    }
  }
  Route get(String path, String[] paths) {
    return find(path, paths, false);
  }
  //-> to split path
  private static final String[] root = {""};
  static String[] paths(String path) {
    if ("".equals(path) || "/".equals(path)) {
      return root;  //-> to match "" with "/".
    }
    //-> to match "/1/2" with "/1/2/".
    /// "/1/2"->"1/2", "/1/2/"->"1/2/",
    String trimed = path.substring(1);
    /// "1/2"->{"1", "2"}, "1/2/"->{"1", "2"}
    return trimed.split("/");
  }
  private boolean match(
    String path, String[] paths, Route rt, boolean checking
  ) {
    if (rt.paths.length != paths.length) return false;
    for (int i=0; i<rt.paths.length; i++) {
      if (rt.paths[i].startsWith(":")) continue;
      if (rt.paths[i].equals(paths[i])) continue;
      if (checking) {
        if (paths[i].startsWith(":")) continue;
      }
      return false;
    }
    return true;
  }
}
