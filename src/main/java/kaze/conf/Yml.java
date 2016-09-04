package kaze.conf;

import java.io.InputStream;
import java.util.LinkedHashMap;

import kaze.Conf;
import kaze.fw.lib.Snake;

public class Yml {

  public LinkedHashMap<String, Object> map;
  
  public static Yml build() {    
    InputStream fw = is("/_conf.yml");
    Yml yml = new Yml(fw);
    
    InputStream app = is("/conf.yml");
    if (app != null) yml.pull(app);
    
    String e = Arg.d("kaze.env");
    if (e != null) {
      InputStream env = is(
        "/conf-" + e + ".yml"
      );
      if (env != null) yml.pull(env);
    }
    
    Arg.push(yml);
    return yml;
  }

  private static InputStream is(String path) {
    return Conf.class.getResourceAsStream(path);
  }

  private Yml(InputStream is) {
    map = Snake.load(is);
  }

  private void pull(InputStream is) {
    map.putAll(Snake.load(is));    
  }
}
