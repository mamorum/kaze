package kaze.http.tool;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import kaze.http.ex.ConvertException;

public class Converter {

  public static final ObjectMapper jcksn;
  
  static {
    jcksn = new ObjectMapper();
    jcksn.setVisibility(
      PropertyAccessor.FIELD,
      JsonAutoDetect.Visibility.ANY
    );
  }
  
  public static String toJson(Object obj) {
    try {
      return jcksn.writeValueAsString(obj);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }
  
  public static <T> T toObj(String json, Class<T> obj) {
    try {
      return jcksn.readValue(json, obj);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }

  public static <T> T convert(Object val, Class<T> type) {
    try {
      return Converter.jcksn.convertValue(val, type);
    } catch (IllegalArgumentException e) {
      throw new ConvertException(e);
    }
  }
}
