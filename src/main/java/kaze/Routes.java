package kaze;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.App.Func;

class Routes {
  private List<Route> paths = new ArrayList<>();
  private Route find(String[] parts, boolean checking) {
    for (Route p: paths) {
      if (p.match(parts, checking)) {
        return p;
      }
    }
    return null;
  }
  //-> add
  void add(String path, Func func) {
    Route add = new Route(path, func);
    Route added = find(add.paths, true);
    if (added == null) {
      paths.add(add);
    } else { ///-> path duplicated
      throw new IllegalStateException(
        "Path duplicated [add=" + path + "] " +
        "[added=" + added.path + "]."
      );
    }
  }
  //-> run
  boolean run(
    HttpServletRequest req, HttpServletResponse res, App app
  ) throws ServletException {
    ///-> find
    String path = Path.get(req);
    String[] paths = Path.split(path);
    Route route = find(paths, false);
    if (route == null) return false;
    ///-> run
    Req rq = new Req(req, app, paths, route);
    Res rs = new Res(res, app);
    try { route.func.exec(rq, rs);  }
    catch (Exception e) { throw new ServletException(e); }
    return true;
  }
}