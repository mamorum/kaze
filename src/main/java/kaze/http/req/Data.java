package kaze.http.req;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kaze.http.ex.ValidateEx;

public class Data<T> {
	
	private T o;
	public Data(T o) { this.o = o; }
	
	public T get() {
		return o;
	}
	
	public T validate() {
	  Set<ConstraintViolation<T>> scv = v.validate(o);
		if (scv.size() == 0) return o;  // valid.
		throw new ValidateEx(scv);  // not.
	}

  private final static Validator v
	  = Validation.buildDefaultValidatorFactory().getValidator();
}
