package kaze.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kaze.http.data.Tool;
import kaze.http.req.Data;
import kaze.http.req.Uri;

public class Req {

	public HttpServletRequest sr;
	public Uri uri;
	
	public Req(HttpServletRequest sr, Uri uri) {
    this.sr = sr;
    this.uri = uri;
	}

  public <T> Data<T> json(Class<T> to) {
    return Data.jsonToObj(sr, to);
  }

  public <T> Data<T> params(Class<T> to) {
    return Data.paramsToObj(sr, to);
  }

  public String uri(String path) {
    return uri.path(path);
  }

  public <T> T uri(String path, Class<T> to) {    
    return Tool.convert(uri.path(path), to);
  }  

  public String param(String name) {
    return sr.getParameter(name);
  }

  public <T> T param(String name, Class<T> to) {
    return Tool.convert(param(name), to);
  }
  
  public List<String> listParam(String name) {
    return Arrays.asList(sr.getParameterValues(name));
  }

  public <T> List<T> listParam(String name, Class<T> to) {
    List<String> ls = listParam(name);
    List<T> lt = new ArrayList<>(ls.size());
    for (String s : ls) lt.add( Tool.convert(s, to) );
    return lt;
  }
}
