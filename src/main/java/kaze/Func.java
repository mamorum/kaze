package kaze;

@FunctionalInterface public interface Func {
  void exec(Req req, Res res) throws Exception;
}
