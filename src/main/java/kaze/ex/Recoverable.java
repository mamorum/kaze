package kaze.ex;

import kaze.http.Res;

public interface Recoverable {
  void reply(Res res);
}
