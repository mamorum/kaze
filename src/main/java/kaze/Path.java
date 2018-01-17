package kaze;

import java.util.Map;

public class Path {
  String path;  // app path
  String[] parts;  // splited app path
  Map<String, Integer> index;  // for path param
  Func func;
  public Path(String appPath, Func func) {
    this.path=appPath; this.func=func;
  }
  public Path(String[] t, Map<String, Integer> i, Func f) {
    parts=t; index=i; func=f;
  }
}