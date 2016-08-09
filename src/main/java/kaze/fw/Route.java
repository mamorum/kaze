package kaze.fw;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.ex.Recoverable;
import kaze.http.req.Uri;

public class Route {
  
  private static final Logger log = LoggerFactory.getLogger(Route.class);
  
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
	
  private Uri uri(String uri) {
    if (uriIndex == null) return new Uri(uri);
    
    // regex uri
    Map<String, String> uriVals = new HashMap<>();    
    String[] paths = uri.substring(1).split("/");
    for (String key : this.uriIndex.keySet()) {
      int index = this.uriIndex.get(key);
      String value = paths[index];
      uriVals.put(key, value);
    }
    return new Uri(uri, uriVals);
  }

  public void run(
      String uri,
      HttpServletRequest sreq,
      HttpServletResponse sres
  ) {
    utf8(sreq, sres);
    Req req = new Req(sreq, uri(uri));
    Res res = new Res(sres);
    try { func.call(req, res); }
    catch (Throwable e) {
      if (e instanceof Recoverable) {
        log.debug(e.getMessage(), e);
        ((Recoverable) e).reply(res);
        return;
      }
      if (e instanceof RuntimeException) {
          throw (RuntimeException) e;
      }
      throw new RuntimeException(e);
    }
  }
  
  private void utf8(
    HttpServletRequest req,
    HttpServletResponse res) 
  {
    if (req.getCharacterEncoding() == null) {
      try { req.setCharacterEncoding(utf8); }
      catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
    res.setCharacterEncoding(utf8);
  }
  
  private static final String utf8 = "UTF-8";
}
