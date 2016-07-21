package kaze.fw.embd;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.fw.Route;
import kaze.fw.Routes;

/*
 * DefaultServlet#init() is called, when server starts.
 */
@SuppressWarnings("serial")
public class JettyServlet extends DefaultServlet {

  private static final Logger logger = LoggerFactory.getLogger(JettyServlet.class);
  
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
    finally {
      log(method, uri,  res.getStatus());
    }
  }
  
  
  void log(String method, String uri, int status) {
    StringBuilder s = new StringBuilder();
    s.append(method).append(" ")
       .append(uri).append(" ").append(status);
    logger.debug(s.toString());
  }
}
