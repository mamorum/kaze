package kaze.route;

import kaze.http.Req;
import kaze.http.Res;

@FunctionalInterface
public interface Func {
  void accept(Req req, Res res) throws Throwable;
}
