package kaze.http.lib;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import kaze.http.ex.ConvertException;

public class Jackson {

  public static final ObjectMapper om;
  
  static {
    om = new ObjectMapper();
    om.setVisibility(
      PropertyAccessor.FIELD,
      JsonAutoDetect.Visibility.ANY
    );
  }
  
  public static String toJson(Object obj) {
    try {
      return om.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static <T> T toObj(String json, Class<T> obj) {
    try {
      return om.readValue(json, obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T convert(Object val, Class<T> type) {
    try {
      return Jackson.om.convertValue(val, type);
    } catch (IllegalArgumentException e) {
      throw new ConvertException(e);
    }
  }
}
