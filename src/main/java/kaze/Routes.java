package kaze;

import java.util.ArrayList;
import java.util.List;

import kaze.App.Func;

class Routes {
  private List<Route> paths = new ArrayList<>();
  private Route find(Route path, boolean checking) {
    for (Route p: paths) {
      if (p.match(path, checking)) {
        return p;
      }
    }
    return null;
  }
  void add(String path, Func func) {
    Route add = new Route(path, func);
    Route added = find(add, true);
    if (added == null) {
      paths.add(add);
    } else {
      throw new IllegalStateException(
        "Path duplicated [add=" + add.path + "] " +
        "[added=" + added.path + "]."
      );
    }
  }
  Route resolve(String path) {
    Route comming = new Route(path);
    Route added = find(comming, false);
    if (added == null) return null;
    comming.inherit(added);
    return comming;
  }
}
