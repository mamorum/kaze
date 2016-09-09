package kaze.http;

import kaze.http.data.Validator;

public class Data<T> {

  private T obj;
  public Data(T obj) { this.obj = obj; }

  public T get() { return obj; }
  public T valid() { Validator.validate(obj); return obj; }
}
