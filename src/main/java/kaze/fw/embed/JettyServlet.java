package kaze.fw.embed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;

import kaze.ex.NoRouteException;
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
    try { routes.run(sreq, sres); }
    catch (NoRouteException e) {
      // serve static contents.
      super.service(sreq, sres);
    }
  }
}
