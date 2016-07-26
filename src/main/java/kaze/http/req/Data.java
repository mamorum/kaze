package kaze.http.req;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kaze.http.ex.ValidateException;

public class Data<T> {
	
	private T o;
	public Data(T src) { this.o = src; }
	
	public T get() { return o; }
	
	public T valid() {
    Set<ConstraintViolation<Object>> scv = v.validate((Object) o);
    if (scv.size() == 0) return o;  // valid.
    throw new ValidateException(scv);  // not.
  }

  static Validator v
    = Validation.buildDefaultValidatorFactory().getValidator();
}
