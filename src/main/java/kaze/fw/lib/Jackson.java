package kaze.fw.lib;

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

  public static <T> T convert(Object val, Class<T> type)
      throws IllegalArgumentException
  {
    return om.convertValue(val, type);
  }
}
