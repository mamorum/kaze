package kaze;

import java.util.HashMap;
import java.util.Map;

class Route {
  String path;
  String[] paths;
  Func func;
  Map<String, Integer> index; //-> for path param
  Route(String path, String[] paths, Func f) {
    this.path=path;
    this.paths=paths;
    this.func=f;
    if (path.contains(":")) { //-> create index for path param
      index = new HashMap<>();
      for (int i=0; i<paths.length; i++) {
        if (paths[i].startsWith(":")) {
          index.put(paths[i], i);
        }
      }
    }
  }
}
