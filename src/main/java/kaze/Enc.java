package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Enc implements Filter {
  String encoding;
  static final String param = "encoding";
  @Override public void init(FilterConfig conf)
  throws ServletException {
    encoding = conf.getInitParameter(param);
    if (encoding == null) {
      throw new ServletException(
        "Init parameter not found. [name=" + param + "]"
      );
    }
  }
  @Override public void destroy() {}
  @Override public void doFilter
    (ServletRequest req, ServletResponse res, FilterChain chain)
  throws IOException, ServletException {
    apply(encoding, req, res);
    chain.doFilter(req, res);
  }
  static void apply(
    String enc, ServletRequest req, ServletResponse res)
  throws UnsupportedEncodingException {
    req.setCharacterEncoding(enc);
    res.setCharacterEncoding(enc);
  }
}
