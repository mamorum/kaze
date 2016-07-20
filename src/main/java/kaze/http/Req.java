package kaze.http;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kaze.fw.lib.Jackson;
import kaze.http.req.Json;
import kaze.http.req.Params;
import kaze.http.req.Uri;

public class Req {

	public HttpServletRequest sreq;
	private Map<String, Integer> uriIndex;
	
	public Req(
	    HttpServletRequest sreq,
	    Map<String, Integer> uriIndex
	) {
    this.sreq = sreq;
    this.uriIndex = uriIndex;
	}

  public Uri uri() {
    return new Uri(sreq.getRequestURI(), uriIndex);
  }

  public Json json() {
    return new Json(sreq);
  }
  
  public String param(String name) {
    return sreq.getParameter(name);
  }

  public <T> T param(String name, Class<T> to) {
    return Jackson.convert(param(name), to);
  }
  
  public Params params() {
    return new Params(sreq);
  }
  
  public List<String> listParam(String name) {
    return Arrays.asList(
        sreq.getParameterValues(name)
    );
  }
  
  public <T> T listParam(String name, Class<T> to) {
    return Jackson.convert(listParam(name), to);
  }
}
