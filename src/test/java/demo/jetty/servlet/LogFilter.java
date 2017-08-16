package demo.jetty.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LogFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}
  @Override
  public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain fc)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) rq;
    System.out.println("Req: " + req.getRequestURI());
    long start = System.currentTimeMillis();
    fc.doFilter(rq, rs);
    long end = System.currentTimeMillis();
    System.out.println("Res: " + (end-start) + "ms");
  }
  @Override
  public void destroy() {}
}
