package kaze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class App extends HttpServlet {
  //-> encoding
  private boolean encode = true;
  private String enc = "utf-8";
  public void disableEncoding() { encode=false; }
  public void encoding(String encoding) { enc=encoding; }
  //-> json converter
  private Json.Parse jsnPrs; private Json.Stringify js;
  public void conv(Json.Parse p, Json.Stringify s) {
    jsnPrs=p; js=s;
  }
  //-> routing
  private Routes get=new Routes(), post=new Routes(),
    put=new Routes(), delete=new Routes();
  public void get(String path, Fn f) { add(path, f, get); }
  public void post(String path, Fn f) { add(path, f, post); }
  public void put(String path, Fn f) { add(path, f, put); }
  public void delete(String path, Fn f) { add(path, f, delete); }
  private void add(String path, Fn f, Routes rts) {
    String[] paths = Routes.paths(path);
    rts.add(path, paths, f);
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
      Req req = new Req(rq, path, paths, rt, jsnPrs);
      Res res = new Res(rs, js);
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