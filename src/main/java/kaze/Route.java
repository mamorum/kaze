package kaze;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.ex.Recoverable;
import kaze.http.Req;
import kaze.http.Res;
import kaze.http.Uri;
import kaze.route.Func;
import kaze.route.Path;
import kaze.route.Routes;

public class Route {

//  public static void build(String... pkgs) {
//    Routes.build(pkgs);
//  }
  
  public static Route get(HttpServletRequest sreq) {
    return Routes.get(
      sreq.getMethod(), sreq.getRequestURI()
    );
  }

  // Instance. Represents @Http and it's java method.
  public String method; // Http Request Method.
  public Path path;  // Http Request URI.
  public Func func;  // Java Method.
  
  public Route(String m, Path p, Func f) {
    this.method = m;
    this.path = p;
    this.func = f;
  }
  
  public void run(
    HttpServletRequest sreq, HttpServletResponse sres
  ) {
    utf8(sreq, sres);
    String uri = sreq.getRequestURI();
    Req req = new Req(sreq, uri(uri));
    Res res = new Res(sres);
    try { func.accept(req, res); }
    catch (Throwable e) {
      if (e instanceof Recoverable) {
        ((Recoverable) e).reply(res);
        return;
      }
      if (e instanceof RuntimeException) {
          throw (RuntimeException) e;
      }
      throw new RuntimeException(e);
    }
  }

  private Uri uri(String uri) {
    if (path.uriIndex == null) return new Uri(uri);
    
    // regex uri
    Map<String, String> uriVals = new HashMap<>();    
    String[] paths = uri.substring(1).split("/");
    for (String key : path.uriIndex.keySet()) {
      int index = path.uriIndex.get(key);
      String value = paths[index];
      uriVals.put(key, value);
    }
    return new Uri(uri, uriVals);
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
