package kaze;

@FunctionalInterface
public interface Func {
  void accept(Req req, Res res) throws Throwable;
}
