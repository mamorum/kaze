package kaze;

@FunctionalInterface  // Function
public interface Fn { void exec(Req req, Res res) throws Exception; }