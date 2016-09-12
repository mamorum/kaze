package kaze.route;

import java.util.Map;
import java.util.regex.Pattern;

public class Path {
  
  public String uri;
  public Pattern uriPattern;
  public Map<String, Integer> uriIndex;
  
  public Path(String uri) {
    this.uri = uri;
  }
  
  public Path(String uri, Map<String, Integer> index) {
    this.uri = uri;
    this.uriPattern = Pattern.compile(uri);
    this.uriIndex = index;
  }
}
