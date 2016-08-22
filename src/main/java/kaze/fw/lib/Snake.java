package kaze.fw.lib;

import java.io.InputStream;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import kaze.fw.Conf;

// for single thread, initializing application conf.
public class Snake {
  
  private static Yaml y = new Yaml();
  
  @SuppressWarnings("unchecked")
  public static LinkedHashMap<String, Object> load(InputStream is) {
    return (LinkedHashMap<String, Object>) y.load(is);
  }
  
  public static Conf loads(InputStream is) {
    return y.loadAs(is, Conf.class);
  }
}
