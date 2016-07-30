package kaze.core;

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
 
  String uri;
  Pattern uriPattern;
  Map<String, Integer> uriIndex;
  Func func;
  
  private Route(String uri, Func func) {
    this.uri = uri; this.func = func;
  }
  
  private Route(String uri, Map<String, Integer> index, Func func) {
    this.uri = uri;
    this.uriPattern = Pattern.compile(uri);
    this.uriIndex = index;
    this.func = func;
  }

  static Route fromUri(String uri, Func func) {
    return new Route(uri, func);
  }
	
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/[^/]+/[^/]+")
  static Route fromRegexUri(String uri, Func func) {
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
    
    String regexUri = sb.toString();
    return new Route(regexUri, uriIndex, func);
	}
	
  private Uri uri(String reqUri) {
    if (uriIndex == null) return new Uri(reqUri);
    
    // regex uri
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
  
  void log(String method) {
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
