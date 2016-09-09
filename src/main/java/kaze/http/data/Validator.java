package kaze.http.data;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import kaze.http.ex.ValidateException;

public class Validator {

  private static final javax.validation.Validator v
    = Validation.buildDefaultValidatorFactory().getValidator();

  public static void validate(Object o) {
    Set<ConstraintViolation<Object>> scv = v.validate(o);
    if (scv.size() != 0) throw new ValidateException(scv);
  }
}
