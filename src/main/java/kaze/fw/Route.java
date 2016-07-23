package kaze.fw;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.req.Uri;

public class Route {

  private static final Logger logger = LoggerFactory.getLogger(Route.class);
 
  public String uri;
	public Pattern uriPattern;
	public Map<String, Integer> uriIndex;
  public Func func;
		
	public static Route fromUri(String uri, Func func) {
	  Route r = new Route();
	  r.uri = uri; r.func = func;
	  return r;
	}
	
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
	public static Route fromRegexUri(String uri, Func func) {
    Map<String, Integer> uriIndex = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    String[] path = uri.substring(1).split("/");
    for (int i = 0; i < path.length; i++) {
      sb.append("/");
      if (path[i].contains(":")) {
        sb.append("[^/]+");
        uriIndex.put(path[i], i);
      } else {
        sb.append(path[i]);
      }
    }
    if (uri.endsWith("/")) sb.append("/");
    
    Route r = new Route();
    r.uri = sb.toString();
    r.uriPattern = Pattern.compile(r.uri);
    r.uriIndex = uriIndex;
    r.func = func;
    return r;
	}
	
	public Uri uri(String reqUri) {
	  if (uriIndex == null) return new Uri(reqUri);
    
    Map<String, String> uriVals = new HashMap<>();    
    String[] paths = reqUri.substring(1).split("/");
    for (String key : this.uriIndex.keySet()) {
      int index = this.uriIndex.get(key);
      String value = paths[index];
      uriVals.put(key, value);
    }
    return new Uri(reqUri, uriVals);
  }

  public void run(
      String reqUri,
      HttpServletRequest req,
      HttpServletResponse res
  ) {
    func.call(
        new Req(req, uri(reqUri)),
        new Res(res)
    );
  }
  
  public void log(String method) {
    if (logger.isDebugEnabled()) {
      String uri4log = this.uri.replaceAll(
          "\\[\\^/\\]\\+", "*"
      );
      log(method, uri4log);
    }
  }

  private void log(String method, String uri4log) {
    logger.debug(
        "[{} {}] -> [{}#{}]", method, uri4log, 
        func.m.getDeclaringClass().getName(), 
        func.m.getName());
  }
}
