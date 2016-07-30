package kaze.core.embed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;

import kaze.core.Route;
import kaze.core.Routes;

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
    
    String method = req.getMethod();
    String uri = req.getRequestURI();    
    Route route = routes.get(method, uri);
    
    try {
      if (route != null) {
        route.run(uri, req, res);
      }
      else {
        // static contents
        super.service(req, res);
      }
    }
    catch (Exception e) {
      res.setStatus(500);
      throw e;
    }
  }
}
