package kaze.http.ex;

import java.util.Set;

import javax.validation.ConstraintViolation;

@SuppressWarnings("serial")
public class ValidateException extends RuntimeException {
  
  public <T> ValidateException(Set<ConstraintViolation<T>> scv) {
    // TODO implement create json source;
  }

}
