package kaze.http;

import javax.servlet.http.HttpServletRequest;

import kaze.fw.lib.Jackson;
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
