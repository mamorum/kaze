package kaze.http.data;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kaze.fw.lib.Jackson;
import kaze.http.ex.ConvertException;
import kaze.http.ex.ValidateException;

public class Tool {

  public static String toJson(Object obj) {
    try {
      return Jackson.om.writeValueAsString(obj);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }
  
  public static <T> T toObj(String json, Class<T> obj) {
    try {
      return Jackson.om.readValue(json, obj);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }

  public static <T> T convert(Object val, Class<T> type) {
    try {
      return Jackson.om.convertValue(val, type);
    } catch (IllegalArgumentException e) {
      throw new ConvertException(e);
    }
  }  

  public static final Validator v
    = Validation.buildDefaultValidatorFactory().getValidator();
  
  public static void validate(Object o) {
    Set<ConstraintViolation<Object>> scv = v.validate(o);
    if (scv.size() != 0) throw new ValidateException(scv);
  }  
}
