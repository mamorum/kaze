package kaze;

import kaze.App.Func;

public class Route {
  public Func func;
  public Path path;

  public Route(Func func, Path path) {
    this.func=func; this.path=path;
  }
}
