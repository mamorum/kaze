package kaze.http.req;

import java.util.HashMap;
import java.util.Map;

public class Uri {

	private Map<String, Integer> index;
	private String uri;
		
	private Uri(Map<String, Integer> index, String uri) {
		this.index = index;
		this.uri = uri;
	}

	public String val(String expression) {
		int i = this.index.get(expression);
		String value = this.uri.substring(1).split("/")[i];
		return value;
	}
	
	// for framework, not for app.
	public static class Factory {
		
		// for one time to cache.
		public static Map<String, Integer> index(String template) {
			Map<String, Integer> createIndex = new HashMap<>();
			String[] parts = template.substring(1).split("/");
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].contains(":")) {
					createIndex.put(parts[i], i);
				}
			}
			return createIndex; 
		}
		
		// for every requests.
		public static Uri create(Map<String, Integer> index, String real) {
			return new Uri(index, real);
		}
	}
}
