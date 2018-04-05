package kaze;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReqTest {
  @Test public void no_json_parser() {
    Req req = new Req(null, new App(), null, null);
    try {
      req.json(Object.class);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      System.out.println("ReqTest#no_json_parser() ->");
      System.out.println(e.getMessage());
    }
  }
  @Test public void path_param_not_found() {
    Route path = new Route(
      "/cat/:name", null
    );
    Req req = new Req(null, new App(), null, path);
    try {
      req.path("param");
      fail();
    } catch (IllegalArgumentException e) {  //-> OK
      System.out.println("ReqTest#path_param_not_found() ->");
      System.out.println(e.getMessage());
    }
  }
  @Test public void path_param_not_defined() {
    Route path = new Route(
      "/cat/tom", null
    );
    Req req = new Req(null, new App(), null, path);
    try {
      req.path("param");
      fail();
    } catch (IllegalStateException e) {  //-> OK
      System.out.println("ReqTest#path_param_not_defined() ->");
      System.out.println(e.getMessage());
    }
  }
}
