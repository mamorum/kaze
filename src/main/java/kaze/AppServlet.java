package kaze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//-> servlet
@SuppressWarnings("serial")
public class AppServlet extends HttpServlet {
  public App app;
  @Override protected void doGet(
    HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
    boolean done = app.doGet(req, res);
    if (!done) res.sendError(404);
  }
  @Override protected void doPost(
    HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
    boolean done = app.doPost(req, res);
    if (!done) res.sendError(404);
  }
  @Override protected void doPut(
    HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
    boolean done = app.doPut(req, res);
    if (!done) res.sendError(404);
  }
  @Override protected void doDelete(
    HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
    boolean done = app.doDelete(req, res);
    if (!done) res.sendError(404);
  }
}