package it.http.res;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.ItCase;
import kaze.http.Req;
import kaze.http.Res;

public class ResExceptionTest extends ItCase {

  @Before public void regist() {
    http.get("/res/ex", ResExceptionTest::ex);
    http.get("/res/rex", ResExceptionTest::rex);
  }

  // checked
  public static void ex(Req req, Res res) throws Exception{
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
  public static void rex(Req req, Res res) {
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
