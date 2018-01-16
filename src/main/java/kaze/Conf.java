package kaze;

public class Conf {
  //-> encoding
  public String encoding = "utf-8";
  //-> json parser
  public void parser(Json2obj j2o, Obj2json o2j) {
    this.j2o=j2o; this.o2j=o2j;
  }
  Json2obj j2o = Conf::noJ2o;
  Obj2json o2j = Conf::noO2j;
  static <T> T noJ2o(String json, Class<T> obj) {
    throw new IllegalStateException(msg);
  }
  static String noO2j(Object obj) {
    throw new IllegalStateException(msg);
  }
  static String msg =
      "No json parser found. Please set parser to App class.";
  @FunctionalInterface public static interface Json2obj {
    <T> T exec(String json, Class<T> obj);
  }
  @FunctionalInterface public static interface Obj2json {
    String exec(Object obj);
  }
}
