package kaze.http.req;

import java.util.Map;

import kaze.fw.lib.Jackson;

public class Uri {

	private Map<String, Integer> index;
	private String uri;
		
	public Uri(String uri, Map<String, Integer> index) {
		this.index = index;
		this.uri = uri;
	}
  
	public String path(String expression) {
		int i = this.index.get(expression);
		String value = this.uri.substring(1).split("/")[i];
		return value;
	}
	
	public <T> T path(String expression, Class<T> to) {
	  return Jackson.convert(path(expression), to);
  }
}
