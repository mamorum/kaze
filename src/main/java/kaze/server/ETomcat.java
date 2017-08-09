package kaze.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import kaze.App;

// Embedded Tomcat
public class ETomcat {
  //-> start
  public static void listen(int port) { listen("0.0.0.0", port); }
  public static void listen(String host, int port) {
    Tomcat cat = new Tomcat();
    cat.setPort(port);
    cat.setHostname(host);
    try {
      String uri = ETomcat.class.getResource("/public").getPath();
      System.out.println(uri);
      File docRoot = new File(uri);
      System.out.println(docRoot.getAbsolutePath());
      Context ctx = cat.addContext("", docRoot.getAbsolutePath());
      Tomcat.addServlet(ctx, "default", new AppDocServlet());
      ctx.addServletMappingDecoded("/", "default");
      cat.start();
      cat.getServer().await();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("serial")
  private static class AppDocServlet extends DefaultServlet {
    @Override protected void doGet(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      System.out.println("doGet: " + req.getRequestURI());
      boolean run = App.run(App.get, req, res);
      if (!run) super.doGet(req, res); // static contents
    }
    @Override protected void doPost(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      boolean run = App.run(App.post, req, res);
      if (!run) super.doGet(req, res); // static contents
    }
    @Override protected void doPut(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      App.run(404, App.put, req, res);
    }
    @Override protected void doDelete(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      App.run(404, App.delete, req, res);
    }
  }
}
