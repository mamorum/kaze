package kaze.http.req;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kaze.http.ex.ValidateException;

public class Data<T> {
	
	private T o;
	public Data(T o) { this.o = o; }
	
	public T get() {
		return this.o;
	}
	
	public T validate() {
	  Set<ConstraintViolation<T>> scv = v.validate(this.o);
		if (scv.size() == 0) return this.o;  // valid.
		throw new ValidateException(scv);  // not.
	}

  private final static Validator v
	  = Validation.buildDefaultValidatorFactory().getValidator();
	
}
