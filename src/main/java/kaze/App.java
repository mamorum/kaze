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
  @Override protected void doGet(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(get, i, o); }
  @Override protected void doPost(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(post, i, o); }
  @Override protected void doPut(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(put, i, o); }
  @Override protected void doDelete(HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException { run(delete, i, o); }
  protected void run(Route r, HttpServletRequest i, HttpServletResponse o)
    throws ServletException, IOException
  {
    boolean run = r.run(i, o, conf);
    if (!run) o.sendError(404);
  }
}