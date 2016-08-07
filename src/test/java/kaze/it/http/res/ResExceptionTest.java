package kaze.it.http.res;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResExceptionTest extends ItCase {

  // checked
  @Http({Method.GET, "/res/ex"})
  public void ex(Req req, Res res) throws Exception{
    throw new Exception();
  }
  @Test  // Jetty ErrorHandler sets type and body. 
  public void ex() {
    HttpReq.get(
        "http://localhost:8080/res/ex"
    ).statusIs(500).typeIs(
        "text/html; charset=ISO-8859-1"
    ).bodyIs("Server Error");
  }
  
  // unchecked
  @Http({Method.GET, "/res/rex"})
  public void rex(Req req, Res res) {
    throw new RuntimeException();
  }
  @Test  // Jetty ErrorHandler sets type and body. 
  public void rex() {
    HttpReq.get(
        "http://localhost:8080/res/rex"
    ).statusIs(500).typeIs(
        "text/html; charset=ISO-8859-1"
    ).bodyIs("Server Error");
  }
}
