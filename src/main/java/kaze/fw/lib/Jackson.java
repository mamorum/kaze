package kaze.fw.lib;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson {

  public static final ObjectMapper om;
  
  static {
    om = new ObjectMapper();
    om.setVisibility(
      PropertyAccessor.FIELD,
      JsonAutoDetect.Visibility.ANY
    );
  }

  public static <T> T toObj(URL jsonFile, Class<T> obj) {
    try {
      return om.readValue(jsonFile, obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
