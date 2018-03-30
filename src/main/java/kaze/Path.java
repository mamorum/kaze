package kaze;

import java.util.HashMap;
import java.util.Map;

import kaze.App.Func;

class Path {
  String path;
  String[] parts;
  Map<String, Integer> index;
  Func func;
  Path(String path, String[] parts, Func func) {
    this.path=path; this.parts=parts; this.func=func;
    if (path.contains(":")) index();
  }
  private void index() {
    index = new HashMap<>();
    for (int i=0; i<parts.length; i++) {
      if (parts[i].startsWith(":")) {
        index.put(parts[i], i);
      }
    }
  }
  boolean match(String[] tParts, boolean checking) {
    if (parts.length != tParts.length) return false;
    for (int i=0; i<parts.length; i++) {
      if (parts[i].startsWith(":")) continue;
      if (parts[i].equals(tParts[i])) continue;
      if (checking) {
        if (tParts[i].startsWith(":")) continue;
      }
      return false;
    }
    return true;
  }
}
