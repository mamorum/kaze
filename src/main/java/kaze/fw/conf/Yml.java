package kaze.fw.conf;

import java.io.InputStream;
import java.util.LinkedHashMap;

import kaze.fw.lib.Snake;

public class Yml {

  public LinkedHashMap<String, Object> map;
  
  public Yml(InputStream is) {
    map = Snake.load(is);
  }

  public void pull(InputStream is) {
    map.putAll(Snake.load(is));    
  }
  
  public int intVal(String key) {
    Object val = map.get(key);
    if (val instanceof Integer) {
      return (Integer) val;
    }
    return Integer.valueOf(
      val(key)
    );
  }
  
  public String val(String key) {
    return (String) map.get(key);
  }
}
