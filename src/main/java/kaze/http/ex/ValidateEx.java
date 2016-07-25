package kaze.http.ex;

import java.util.Set;

import javax.validation.ConstraintViolation;

@SuppressWarnings("serial")
public class ValidateEx extends RuntimeException {
  
  public <T> ValidateEx(Set<ConstraintViolation<T>> scv) {
    // TODO implement create json source;
  }

}
