package kaze.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kaze.http.io.Converter;
import kaze.http.io.Json;
import kaze.http.io.Params;

public class Req {

	public HttpServletRequest sr;
	public Map<String, Integer> uriIndex;
	public Req(HttpServletRequest sr, Map<String, Integer> uriIndex) {
	  this.sr = sr;
	  this.uriIndex = uriIndex;
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

  public String path(String var) {
    // TODO if (uriIndex == null) throw ... ?
    return Uri.var(var, sr.getRequestURI(), uriIndex);
  }

  public <T> T path(String var, Class<T> to) {    
    return Converter.convert(path(var), to);
  }  
  
  private static class Uri {
    static String var(String var, String uri, Map<String, Integer> uriIndex) {    
      String[] paths = uri.substring(1).split("/");
      int index = uriIndex.get(var);
      return paths[index];
    }
  }
}
