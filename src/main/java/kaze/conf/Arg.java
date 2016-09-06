package kaze.conf;

import java.util.Properties;

public class Arg {
  
  // return val from arg "-Dkey=val".
  public static String d(String key) {
    return System.getProperty(key);
  }

  // overwrite properties with system properties.
  public static void push(Properties p) {
    for (String key: p.stringPropertyNames()) {
      String arg = d(key);
      if (arg != null) p.setProperty(key, arg);
    }
  }
}
