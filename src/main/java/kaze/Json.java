package kaze;

public class Json { // json parser
  @FunctionalInterface public interface ToObj {
    <T> T exec(String json, Class<T> toObj);
  }
  @FunctionalInterface public interface FromObj {
    String exec(Object fromObj);
  }
  public void parser(ToObj fromJson, FromObj toJson) {
    this.toObj=fromJson; this.fromObj=toJson;
  }
  ToObj toObj = Json::noToObj;
  FromObj fromObj = Json::noFromObj;
  private static String msg = "No json parser found.";
  private static <T> T noToObj(String json, Class<T> toObj) {
    throw new IllegalStateException(msg);
  }
  private static String noFromObj(Object fromObj) {
    throw new IllegalStateException(msg);
  }
}
