package kaze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.json.J2o;
import kaze.json.O2j;

@SuppressWarnings("serial")
public class App extends HttpServlet {
  //-> encoding
  private boolean encode = true;
  private String enc = "utf-8";
  public void disableEncoding() { encode=false; }
  public void encoding(String encoding) { enc=encoding; }
  //-> json converter
  private J2o j2o; private O2j o2j;
  public void conv(J2o jsonToObj, O2j objToJson) {
    this.j2o=jsonToObj; this.o2j=objToJson;
  }
  //-> routing
  private Routes get=new Routes(), post=new Routes(),
    put=new Routes(), delete=new Routes();
  public void get(String path, Fn func) { add(path, func, get); }
  public void post(String path, Fn func) { add(path, func, post); }
  public void put(String path, Fn func) { add(path, func, put); }
  public void delete(String path, Fn func) { add(path, func, delete); }
  private void add(String path, Fn func, Routes rts) {
    String[] paths = Routes.paths(path);
    rts.add(path, paths, func);
  }
  private void run(
    Routes rts, HttpServletRequest rq, HttpServletResponse rs)
  throws ServletException, IOException {
    String path = path(rq);
    String[] paths = Routes.paths(path);
    Route rt = rts.get(path, paths);
    if (rt == null) {
      rs.setStatus(404);
    } else {
      if (encode) Enc.apply(enc, rq, rs);
      Req req = new Req(rq, paths, rt, j2o);
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