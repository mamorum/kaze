package kaze.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import kaze.Conf;

public class Property {
  
  public static Properties load() {    
    Properties fw =  load(
      is("/_conf.properties")
    );
    
    InputStream app = is("/conf.properties");
    push(app, fw);

    String e = Arg.d("kaze.env");
    if (e == null) return fw;
    InputStream env = is(
      "/conf-" + e + ".properties"
    );
    push(env, fw);

    return fw;
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
