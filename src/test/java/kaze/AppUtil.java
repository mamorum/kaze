package kaze;

public class AppUtil {
  public static void noJsonParser(App app) {
    app.json2obj = App::noJson2obj;
    app.obj2json = App::noObj2json;
  }
}
