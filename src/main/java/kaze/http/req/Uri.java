package kaze.http.req;

import java.util.Map;

public class Uri {

  private String uri;
	private Map<String, Integer> index;
		
	public Uri(String uri, Map<String, Integer> index) {
    this.uri = uri;
		this.index = index;
	}
  
	public String path(String expression) {
		int i = this.index.get(expression);
		String value = this.uri.substring(1).split("/")[i];
		return value;
	}
}
