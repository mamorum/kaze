package kaze.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kaze.http.io.Converter;
import kaze.http.io.Json;
import kaze.http.io.Params;

public class Req {

	public HttpServletRequest sr;
	public Uri uri;
	
	public Req(HttpServletRequest sr, Uri uri) {
    this.sr = sr;
    this.uri = uri;
	}

  public <T> Input<T> json(Class<T> to) {
    return new Input<>(
      Json.bind(sr, to)
    );
  }

  public <T> Input<T> params(Class<T> to) {
    return new Input<>(
      Params.bind(sr, to)
    );
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
    for (String s : ls) lt.add( Converter.convert(s, to) );
    return lt;
  }
}
