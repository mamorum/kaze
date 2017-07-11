package kaze;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.Req.Path;

public class Route {
  Func func;
  Map<String, Integer> index;  // for regex path

  public Route(Func f, Map<String, Integer> i) {
    func = f; index = i;
  }

  public void run(
      HttpServletRequest req, HttpServletResponse res
  ) {
      Path p = new Path(
        req.getRequestURI(), index
      );
      Req i = new Req(req, p);
      Res o = new Res(res);
      try {
        utf8(req, res);
        func.accept(i, o);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  private static final String utf8 = "UTF-8";
  private void utf8(
    HttpServletRequest req, HttpServletResponse res)
    throws UnsupportedEncodingException
  {
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(utf8);
    }
    res.setCharacterEncoding(utf8);
  }
}
