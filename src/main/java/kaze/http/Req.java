package kaze.http;

import javax.servlet.http.HttpServletRequest;

import kaze.http.lib.Jackson;
import kaze.http.lib.Validator;
import kaze.http.req.Json;
import kaze.http.req.Params;
import kaze.http.req.Uri;

public class Req {

	public HttpServletRequest sreq;
	public Uri uri;
	
	public Req(HttpServletRequest sreq, Uri uri) {
    this.sreq = sreq;
    this.uri = uri;
	}

  public class Data<T> {
    private T o;
    public Data(T o) { this.o = o; }
    public T get() { return o; }
    public T valid() { return Validator.valid(o); }
  }
  
  public <T> Data<T> json(Class<T> to) {
    return new Data<>(Json.convert(sreq, to));
  }

  public <T> Data<T> params(Class<T> to) {
    return new Data<>(Params.convert(sreq, to));
  }
  
  public String param(String name) {
    return sreq.getParameter(name);
  }

  public <T> T param(String name, Class<T> to) {
    return Jackson.convert(param(name), to);
  }
  
  public String[] arrayParam(String name) {
    return sreq.getParameterValues(name);
  }
  
  public <T> T arrayParam(String name, Class<T> to) {
    return Jackson.convert(arrayParam(name), to);
  }

  public String uri(String expr) {
    return uri.path(expr);
  }
  
  public <T> T uri(String expr, Class<T> to) {    
    return Jackson.convert(uri.path(expr), to);
  }  
}
