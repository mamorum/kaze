package kaze.it.http.res;

import org.junit.Test;

import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResNotFoundTest extends ItCase {
  
  @Test  // Jetty ErrorHandler sets type and body. 
  public void notFound() {
    HttpReq.get(
        "http://localhost:8080/res/not/found"
    ).statusIs(404).typeIs(
        "text/html; charset=ISO-8859-1"
    ).bodyIs("Not Found").close();
  }
}
