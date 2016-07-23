package kaze.http.req;

import java.util.Map;

public class Uri {

  public String string;
	public Map<String, String> values;

  public String path(String expression) {
    if (values == null) throw new RuntimeException(
        "URI [" + string + "] has no values to return."
    );
    return this.values.get(expression);
  }

	public Uri(String reqUri) {
    this.string = reqUri;
	}

  public Uri(String reqUri, Map<String, String> values) {
    this.string = reqUri;
    this.values = values;
  }
  
}
