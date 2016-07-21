package kaze.http;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kaze.fw.lib.Jackson;
import kaze.http.req.Json;
import kaze.http.req.Params;
import kaze.http.req.Uri;

public class Req {

	public HttpServletRequest sreq;
	private Uri uri;
	
	public Req(HttpServletRequest sreq, Uri uri) {
    this.sreq = sreq;
    this.uri = uri;
	}

  public String uri(String path) {
    return uri.path(path);
  }
  
  public <T> T uri(String path, Class<T> to) {
    return Jackson.convert(uri.path(path), to);
  }

  public Json json() {
    return new Json(sreq);
  }

  public Params params() {
    return new Params(sreq);
  }
  
  public String param(String name) {
    return sreq.getParameter(name);
  }

  public <T> T param(String name, Class<T> to) {
    return Jackson.convert(param(name), to);
  }
  
  public List<String> listParam(String name) {
    return Arrays.asList(sreq.getParameterValues(name));
  }
  
  public <T> T listParam(String name, Class<T> to) {
    return Jackson.convert(listParam(name), to);
  }
}
