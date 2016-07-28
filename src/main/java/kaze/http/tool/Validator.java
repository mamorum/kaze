package kaze.http.tool;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import kaze.http.ex.ValidateException;

public class Validator {
  
  public static <T> T validate(T o) {
    Set<ConstraintViolation<Object>> scv = v.validate((Object) o);
    if (scv.size() == 0) return o;  // valid.
    throw new ValidateException(scv);  // not.
  }
  
  static javax.validation.Validator v
    = Validation.buildDefaultValidatorFactory().getValidator();
}
