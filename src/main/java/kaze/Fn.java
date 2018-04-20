package kaze;

@FunctionalInterface public interface Fn {
  void exec(Req req, Res res) throws Exception;
}