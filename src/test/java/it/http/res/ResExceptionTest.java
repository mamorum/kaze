package it.http.res;

import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.ItCase;
import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;

public class ResExceptionTest extends ItCase {

  // checked
  @Http({Method.GET, "/res/ex"})
  public void ex(Req req, Res res) throws Exception{
    throw new Exception("Threw Exception for test.");
  }
  @Test  // Jetty ErrorHandler sets type and body. 
  public void ex() {
    HttpReq.get(
        "http://localhost:8080/res/ex"
    ).statusIs(500).typeIs(
        "text/html; charset=ISO-8859-1"
    ).bodyIs("Server Error").close();
  }
  
  // unchecked
  @Http({Method.GET, "/res/rex"})
  public void rex(Req req, Res res) {
    throw new RuntimeException(
      "Threw RuntimeException for test."
    );
  }
  @Test  // Jetty ErrorHandler sets type and body. 
  public void rex() {
    HttpReq.get(
        "http://localhost:8080/res/rex"
    ).statusIs(500).typeIs(
        "text/html; charset=ISO-8859-1"
    ).bodyIs("Server Error").close();
  }
}
