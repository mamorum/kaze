package kaze.fw.ex;

import kaze.http.Req;
import kaze.http.Res;

public interface Recoverable {
  void respond(Req req, Res res);
  
  @FunctionalInterface
  public interface Response<Q, S, T> {
    void apply(Q req, S res, T err);
  }
}
