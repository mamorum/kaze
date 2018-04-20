package kaze;

public class Json {
  @FunctionalInterface public interface Parse {
    <T> T exec(String json, Class<T> toObj);
  }
  @FunctionalInterface public interface Stringify {
    String exec(Object from);
  }
  private Json() {}
}
