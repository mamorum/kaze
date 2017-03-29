package kaze.route;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.ex.Recoverable;
import kaze.http.Req;
import kaze.http.Res;

public class Route {

  Func func;
  Map<String, Integer> uriIndex;
  
  public void run(
      HttpServletRequest sreq, HttpServletResponse sres
  ) {
      Utf8Filter.utf8(sreq, sres);
      Req req = new Req(sreq, uriIndex);
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
  
  public static class Plain extends Route {
    Plain(Func f) { this.func = f;}
  }
    
  public static class Regex extends Route {
    public Pattern uriPattern;
    public Regex(String uri, Map<String, Integer> index, Func f) {
      this.uriPattern = Pattern.compile(uri);
      this.uriIndex = index;
      this.func = f;
    }
  }
}
