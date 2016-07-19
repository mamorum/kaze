package kaze.fw.lib;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;

import kaze.fw.Func;
import kaze.fw.Routes;
import kaze.http.Req;
import kaze.http.Res;

/*
 * DefaultServlet#init() is called, when server starts.
 */
@SuppressWarnings("serial")
public class JettyServlet extends DefaultServlet {

  private Routes routes;   
  public JettyServlet(Routes routes) {
    super();
    this.routes = routes;
  }
    
  protected void service(
    HttpServletRequest req, HttpServletResponse res
  ) throws ServletException, IOException {
    
    StringBuilder access = new StringBuilder();

    String method = req.getMethod();
    String uri = req.getRequestURI();
    access.append(method).append(" ").append(uri);
    
    Func func = routes.get(method, uri);
    
    if (func == null) { 
      if ("GET".equals(method) || "POST".equals(method)) {
        super.doGet(req, res);
        return;
      }
    }
    
    try {
      func.call(
        new Req(req, func.uriIndex),
        new Res(res)
      );
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    
    log(access, res.getStatus());
  }
  
  
  void log(StringBuilder s, int status) {
    System.out.println(s.append(" ").append(status));
  }
}
