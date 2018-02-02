package kaze;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class App extends HttpServlet {
  //-> settings
  public String encoding = "utf-8";
  public Json json = new Json();
  public Route get=new Route(), post=new Route(),
    put=new Route(), delete=new Route();
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
    Route rt, HttpServletRequest rq, HttpServletResponse rs)
  throws ServletException, IOException {
    encoding(rq, rs);
    boolean run = rt.run(rq, rs, this);
    if (!run) rs.sendError(404);
  }
  protected void encoding(
    HttpServletRequest req, HttpServletResponse res)
  throws UnsupportedEncodingException {
    if (req.getCharacterEncoding() == null) {
      req.setCharacterEncoding(encoding);
    }
    res.setCharacterEncoding(encoding);
  }
}