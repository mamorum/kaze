package kaze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class App extends HttpServlet {
  //-> conf
  public Conf conf = new Conf();
  //-> routing
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
  protected void run(Route rt, HttpServletRequest rq, HttpServletResponse rs)
    throws ServletException, IOException
  {
    boolean run = rt.run(rq, rs, conf);
    if (!run) rs.sendError(404);
  }
}