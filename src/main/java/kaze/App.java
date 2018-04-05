package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
  String enc = "utf-8";
  public void enc(String encoding) {
    this.enc=encoding;
  }
  ///-> converter
  Json2obj j2o; Obj2json o2j;
  public void conv(Json2obj toObj, Obj2json toJson) {
    this.j2o=toObj; this.o2j=toJson;
  }
  ///-> routing
  Routes get=new Routes(), post=new Routes(),
    put=new Routes(), delete=new Routes();
  public void get(String path, Func func) { get.add(path, func); }
  public void post(String path, Func func) { post.add(path, func); }
  public void put(String path, Func func) { put.add(path, func); }
  public void delete(String path, Func func) { delete.add(path, func); }
  //-> servlet api
  @Override protected void doGet(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(get, rq, rs); }
  @Override protected void doPost(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(post, rq, rs); }
  @Override protected void doPut(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(put, rq, rs); }
  @Override protected void doDelete(HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException { run(delete, rq, rs); }
  protected void run(
    Routes rt, HttpServletRequest rq, HttpServletResponse rs)
  throws ServletException, IOException {
    encoding(rq, rs);
    boolean run = rt.run(rq, rs, this);
    if (!run) rs.sendError(404);
  }
  protected void encoding(
    HttpServletRequest req, HttpServletResponse res)
  throws UnsupportedEncodingException {
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(enc);
    }
    res.setCharacterEncoding(enc);
  }
}