package kaze.http.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Tool {

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
      throw new ConvertException(e);
    }
  }
  
  public static <T> T toObj(String json, Class<T> obj) {
    try {
      return om.readValue(json, obj);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }

  public static <T> T convert(Object val, Class<T> type) {
    try {
      return om.convertValue(val, type);
    } catch (IllegalArgumentException e) {
      throw new ConvertException(e);
    }
  }  

  public static final javax.validation.Validator v
    = Validation.buildDefaultValidatorFactory().getValidator();
  
  public static void validate(Object o) {
    Set<ConstraintViolation<Object>> scv = v.validate(o);
    if (scv.size() != 0) throw new ValidateException(scv);
  }  
}
