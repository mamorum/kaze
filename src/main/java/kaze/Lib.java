package kaze;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.google.gson.Gson;

public class Lib {
  //-> Gson
  public static final Gson gsn = new Gson();

  //-> Jetty
  public static void startJetty(
      String host, int port, int httpTime,
      int max, int min, int threadTime,
      String path, File dir
    ) {
      Server sv = new Server(
        new QueuedThreadPool(max, min, threadTime)
      );
      HttpConfiguration c = new HttpConfiguration();
      c.setSendServerVersion(false);  // security
      //-> connecter
      HttpConnectionFactory fac = new HttpConnectionFactory(c);
      ServerConnector con = new ServerConnector(sv, fac);
      if (host != null) con.setHost(host);
      con.setPort(port);
      con.setIdleTimeout(httpTime);
      sv.setConnectors(new Connector[] { con });
      //-> handler, servlet
      HandlerCollection hc = new HandlerCollection();
      ServletContextHandler sch = new ServletContextHandler(
        ServletContextHandler.SESSIONS
      );
      ServletHolder sh = new ServletHolder(
        "default", new Servlet()
      );
      sh.setInitParameter("dirAllowed", "false"); // security
      sch.setBaseResource(location(path, dir));
      sch.addServlet(sh, "/");
      sch.setErrorHandler(new ErrHandler());
      hc.setHandlers(new Handler[]{ sch });
      sv.setHandler(hc);
      try {
        sv.start();
        sv.join();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    private static Resource location(String path, File dir) {
      if (path != null) return Resource.newClassPathResource(path);
      else if (dir != null) return Resource.newResource(dir);
      else return null;
    }

    @SuppressWarnings("serial")
    private static class Servlet extends DefaultServlet {
      protected void service(
        HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
      {
        String method = req.getMethod();
        String uri = req.getRequestURI();
        Route r = Routes.plainUriRoute(method, uri);
        if (r == null) r = Routes.regexUriRoute(method, uri);
        if (r == null) super.service(req, res);  // static contents
        else r.run(req, res);
      }
    }
    // TODO delete?
    private static class ErrHandler extends ErrorHandler {
      @Override public void handleErrorPage(
        HttpServletRequest req, Writer writer,
        int code, String msg) throws IOException
      {
        if (msg == null) {
          msg = HttpStatus.getMessage(code);
        }
        writer.write(msg);
      }
    }
}
