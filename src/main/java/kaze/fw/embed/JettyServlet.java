package kaze.fw.embed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;

import kaze.App;
import kaze.fw.Route;

/*
 * DefaultServlet#init() is called, when server starts.
 */
@SuppressWarnings("serial")
public class JettyServlet extends DefaultServlet {

  protected void service(
    HttpServletRequest req, HttpServletResponse res
  ) throws ServletException, IOException {
    
    // TODO Routeの解決と処理は、Routes内で完結するようにする。
    // -> 他のサーブレットでも Routes が使えるように。
    String method = req.getMethod();
    String uri = req.getRequestURI();    
    Route route = App.routes.get(method, uri);
    
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
