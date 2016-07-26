package kaze.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kaze.http.lib.Converter;
import kaze.http.lib.Validator;
import kaze.http.req.Json;
import kaze.http.req.Params;
import kaze.http.req.Uri;

public class Req {

	public HttpServletRequest sr;
	private Uri uri;
	
	public Req(HttpServletRequest sr, Uri uri) {
    this.sr = sr;
    this.uri = uri;
	}

  public <T> Data<T> json(Class<T> to) {
    return new Data<>(
        Json.of(sr).to(to)
    );
  }

  public <T> Data<T> params(Class<T> to) {
    return new Data<>(
        Params.of(sr).to(to)
    );
  }

  public class Data<T> {
    private T o;
    public Data(T o) { this.o = o; }
    public T get() { return o; }
    public T valid() { return Validator.validate(o); }
  }

  public String uri(String path) {
    return uri.path(path);
  }

  public <T> T uri(String path, Class<T> to) {    
    return Converter.convert(uri.path(path), to);
  }  

  public String param(String name) {
    return sr.getParameter(name);
  }

  public <T> T param(String name, Class<T> to) {
    return Converter.convert(param(name), to);
  }
  
  public List<String> listParam(String name) {
    return Arrays.asList(sr.getParameterValues(name));
  }

  public <T> List<T> listParam(String name, Class<T> to) {
    List<String> ls = listParam(name);
    List<T> lt = new ArrayList<>(ls.size());
    ls.forEach(s -> { lt.add(Converter.convert(s, to)); });
    return lt;
  }
}
