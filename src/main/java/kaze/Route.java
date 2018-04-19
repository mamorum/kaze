package kaze;

import java.util.HashMap;
import java.util.Map;

class Route {
  String path;
  String[] paths;
  Fn func;
  Map<String, Integer> index; //-> for path param
  Route(String path, String[] paths, Fn f) {
    this.path=path;
    this.paths=paths;
    this.func=f;
    if (path.contains(":")) {
      createIndex();
    }
  }
  private void createIndex() {
    index = new HashMap<>();
    for (int i=0; i<paths.length; i++) {
      if (paths[i].startsWith(":")) {
        index.put(paths[i], i);
      }
    }
  }
}
