package kaze;

import java.util.Map;

public class Route {
  public Func func;
  public String path;
  public String[] paths;
  public Map<String, Integer> params;
  public Route(
    Func func, String path,String[] paths,
    Map<String, Integer> params
  ) {
    this.func=func; this.path=path;
    this.paths=paths; this.params=params;
  }

  @FunctionalInterface public interface Func {
    void accept(Req req, Res res) throws Exception;
  }
}
