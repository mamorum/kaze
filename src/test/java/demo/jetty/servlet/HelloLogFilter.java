package demo.jetty.servlet;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class HelloLogFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}
  @Override
  public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain fc)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) rq;
    System.out.print(LocalDateTime.now());
    System.out.print(" Before ");
    System.out.println(req.getRequestURI());
    long start = System.currentTimeMillis();
    fc.doFilter(rq, rs);
    long end = System.currentTimeMillis();
    System.out.print(LocalDateTime.now());
    System.out.print(" After ");
    System.out.print(req.getRequestURI());
    System.out.print(" @");
    System.out.print((end - start));
    System.out.println("ms");
  }
  @Override
  public void destroy() {}
}
