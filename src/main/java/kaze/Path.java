package kaze;

import java.util.Map;

public class Path {
  String uri;
  Func func;
  String[] tree;
  Map<String, Integer> index;  // for path param
  public Path(String uri, Func f) {
    this.uri=uri; this.func=f;
  }
  public Path(String[] t, Map<String, Integer> i, Func f) {
    tree=t; index=i; func=f;
  }
}