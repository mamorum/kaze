package kaze.http.req;

import java.util.Map;

public class Uri {

  public String string;
  public Map<String, String> vals;

  public String path(String expr) {
    if (vals == null) throw new RuntimeException(
        "URI [" + string + "] has no values to return."
    );
    String val = vals.get(expr);
    if (val == null) throw new RuntimeException(
        "Expression [" + expr + "] has no value to return."
    );
    return val;
  }

	public Uri(String reqUri) {
    this.string = reqUri;
	}

  public Uri(String reqUri, Map<String, String> vals) {
    this.string = reqUri;
    this.vals = vals;
  }
}
