package kaze.http;

import kaze.http.io.Validator;

public class Input<T> {

  private T obj;
  public Input(T obj) { this.obj = obj; }

  public T get() { return obj; }
  public T valid() { Validator.validate(obj); return obj; }
}
