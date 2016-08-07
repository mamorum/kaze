package kaze.fw.embed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;

import kaze.fw.Route;
import kaze.fw.Routes;

@SuppressWarnings("serial")
public class JettyServlet extends DefaultServlet {

  private Routes routes;
  public JettyServlet(Routes r) {
    super();
    this.routes = r; 
  }
  
  protected void service(
    HttpServletRequest sreq, HttpServletResponse sres)
    throws ServletException, IOException
  {
    String m = sreq.getMethod();
    String uri = sreq.getRequestURI();
    Route route = routes.route(m, uri);    
    if (route != null) route.run(uri, sreq, sres);
    else super.service(sreq, sres);  // static contens
  }
}
