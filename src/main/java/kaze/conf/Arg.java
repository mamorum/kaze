package kaze.conf;

public class Arg {
  
  // return val from arg "-Dkey=val".
  public static String d(String key) {
    return System.getProperty(key);
  }

  // overwrite yml with system properties.
  public static void push(Yml yml) {
    for (String key: yml.map.keySet()) {
      String arg = d(key);
      if (arg != null) yml.map.put(key, arg);
    }
  }
}
