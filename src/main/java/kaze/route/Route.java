package kaze.route;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.Func;
import kaze.ex.Recoverable;
import kaze.http.Req;
import kaze.http.Res;

public class Route {

  Func func;
  Map<String, Integer> uriIndex;  // for regex uri pattern
  
  public Route(Func f, Map<String, Integer> index) {
    this.func = f;
    this.uriIndex = index;
  }
  
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
}
