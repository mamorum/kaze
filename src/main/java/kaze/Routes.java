package kaze;

import java.util.ArrayList;
import java.util.List;

import kaze.App.Func;

class Routes {
  private List<Route> routes = new ArrayList<>();
  private Route find(Route r, boolean checking) {
    for (Route p: routes) {
      if (p.match(r, checking)) {
        return p;
      }
    }
    return null;
  }
  void add(String path, Func f) {
    Route add = new Route(path, f);
    Route added = find(add, true);
    if (added == null) {
      routes.add(add);
    } else {
      throw new IllegalStateException(
        "Path duplicated [add=" + add.path + "] " +
        "[added=" + added.path + "]."
      );
    }
  }
  Route resolve(String path) {
    Route run = new Route(path);
    Route added = find(run, false);
    if (added == null) return null;
    run.inherit(added);
    return run;
  }
}
