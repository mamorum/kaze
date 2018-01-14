package kaze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class App extends HttpServlet {
  //-> routing
  public Route get=new Route(), post=new Route(),
    put=new Route(), delete=new Route();
  //-> servlet api
  @Override protected void doGet(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(get, i, o); }
  @Override protected void doPost(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(post, i, o); }
  @Override protected void doPut(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(put, i, o); }
  @Override protected void doDelete(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(delete, i, o); }
  protected void run(Route r, HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException {
    Req req = Req.from(i, this);
    Res res = Res.from(o, this);
    String uri = uri(i);
    Func f = r.staticUri.get(uri);
    if (f == null) f = r.find(uri, req);
    if (f == null) o.sendError(404);
    try { f.exec(req, res);  }
    catch (Exception e) { throw new ServletException(e); }
  }
  private String uri(HttpServletRequest i) {
    String c = i.getContextPath(); //-> /ctx
    String s = i.getServletPath(); //-> /srv
    String u = i.getRequestURI(); //-> /ctx/srv/one/two
    return u.substring(c.length() + s.length()); //-> /one/two
  }
  //-> encoding
  public String encoding = "utf-8";
  //-> json parser
  public void parser(Json2obj j2o, Obj2json o2j) {
    this.j2o=j2o; this.o2j=o2j;
  }
  Json2obj j2o = App::noJ2o;
  Obj2json o2j = App::noO2j;
  static <T> T noJ2o(String json, Class<T> obj) {
    throw new IllegalStateException(msg);
  }
  static String noO2j(Object obj) {
    throw new IllegalStateException(msg);
  }
  static String msg =
      "No json parser found. Please set parser to App class.";
  @FunctionalInterface public static interface Json2obj {
    <T> T exec(String json, Class<T> obj);
  }
  @FunctionalInterface public static interface Obj2json {
    String exec(Object obj);
  }
}