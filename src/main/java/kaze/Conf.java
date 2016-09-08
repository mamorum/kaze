package kaze;

import java.util.Properties;

import kaze.conf.Arg;
import kaze.conf.Pfile;

public class Conf {

  private static Properties kv;
  
  static {
    kv = Pfile.load();
    Arg.push(kv);
  }
  
  public static String get(String key) {
    String v = kv.getProperty(key);
    if (v == null) return null;
    if (v.length() == 0) return null;
    return v;
  }
  
  // return 0, if value is null or empty 
  public static int getInt(String key) {
    String v = get(key);
    if (v == null) return 0;
    return Integer.valueOf(v);
  }
}
