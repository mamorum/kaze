package kaze.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import kaze.Conf;

public class Pfile {
  
  public static Properties load() {    
    Properties prop =  load(
      is("/_conf.properties")
    );
    
    InputStream app = is("/conf.properties");
    push(app, prop);

    String e = Arg.d("kaze.env");
    if (e == null) return prop;
    InputStream env = is(
      "/conf-" + e + ".properties"
    );
    push(env, prop);

    return prop;
  }

  private static InputStream is(String path) {
    return Conf.class.getResourceAsStream(path);
  }
  
  private static void push(InputStream is, Properties dst) {
    if (is == null) return;
    Properties p = load(is);
    dst.putAll(p);
  }
  
  private static Properties load(InputStream is) {
    Properties p = new Properties();
    try {
      p.load(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return p;
  }
}
