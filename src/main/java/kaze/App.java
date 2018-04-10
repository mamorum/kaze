package kaze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class App extends HttpServlet {
  //-> functions
  @FunctionalInterface
  public interface Func { void exec(Req req, Res res) throws Exception; }
  @FunctionalInterface
  public interface Json2obj { <T> T exec(String json, Class<T> to); }
  @FunctionalInterface
  public interface Obj2json { String exec(Object obj); }
  //-> settings
  ///-> encoding
  private boolean encode = true;
  private String enc = "utf-8";
  public void disableEncoding() { encode=false; }
  public void encoding(String encoding) { enc=encoding; }
  ///-> json converter
  private Json2obj j2o; private Obj2json o2j;
  public void conv(Json2obj toObj, Obj2json toJson) {
    this.j2o=toObj; this.o2j=toJson;
  }
  //-> routing
  private Routes get=new Routes(), post=new Routes(),
    put=new Routes(), delete=new Routes();
  public void get(String path, Func func) { add(path, func, get); }
  public void post(String path, Func func) { add(path, func, post); }
  public void put(String path, Func func) { add(path, func, put); }
  public void delete(String path, Func func) { add(path, func, delete); }
  private void add(String path, Func func, Routes routes) {
    routes.add(path, func);
  }
  private void run(
    Routes rts, HttpServletRequest rq, HttpServletResponse rs)
  throws ServletException, IOException {
    Route rt = rts.resolve(path(rq));
    if (rt == null) {
      rs.sendError(404);
    } else {
      if (encode) Enc.apply(enc, rq, rs);
      Req req = new Req(rq, j2o, rt);
      Res res = new Res(rs, o2j);
      try { rt.func.exec(req, res); }
      catch (Exception e) { throw new ServletException(e); }
    }
  }
  private String path(HttpServletRequest req) {
    String c = req.getContextPath(); //-> /context
    String s = req.getServletPath(); //-> /servlet
    String r = req.getRequestURI(); //-> /context/servlet/1/2
    String path = r.substring( //-> /1/2
      c.length() + s.length()
    );
    return path;
  }
  //-> servlet api
  @Override protected void doGet(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(get, rq, rs); }
  @Override protected void doPost(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(post, rq, rs); }
  @Override protected void doPut(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(put, rq, rs); }
  @Override protected void doDelete(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(delete, rq, rs); }
}