package kaze.route;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO delete.
public class Utf8Filter {
  static void utf8(
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
