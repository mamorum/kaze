package kaze.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import kaze.App;

// Embedded Tomcat
public class ETomcat {
  //-> settings
  ////-> location of static files
  private static String doc;
  public static void location(String classpathdir) {
    String path = ETomcat.class.getResource(classpathdir).getPath();
    if (path.startsWith("file:")) path = path.substring(5);
    doc = (new File(path)).getAbsolutePath();
  }
  public static void location(File dir) {
    doc = dir.getAbsolutePath();
  }

  //-> start
  public static void listen(int port) { listen("0.0.0.0", port); }
  public static void listen(String host, int port) {
    Tomcat cat = new Tomcat();
    cat.setPort(port);
    cat.setHostname(host);
    System.out.println(doc);
    Context ctx = cat.addContext("", doc);
    if (doc == null) {
      Tomcat.addServlet(ctx, "app", new App.Servlet());
    } else {
      Tomcat.addServlet(ctx, "app", new AppDocServlet());
    }
    ctx.addServletMappingDecoded("/", "app");
    try { cat.start(); }
    catch (LifecycleException e) {
      throw new RuntimeException(e);
    }
    cat.getServer().await();
  }

  @SuppressWarnings("serial")
  private static class AppDocServlet extends DefaultServlet {
    @Override protected void doGet(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
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
      App.run(App.put, req, res, 404);
    }
    @Override protected void doDelete(
      HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
      App.run(App.delete, req, res, 404);
    }
  }
}
