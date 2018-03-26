package kaze;

import static org.junit.Assert.*;

import java.util.HashMap;

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
  @Test public void no_path_param() {
    Req req = new Req(
      null, new App(), new HashMap<>(), null
    );
    try {
      req.path("param");
      fail();
    } catch (IllegalArgumentException e) {  //-> OK
      System.out.println("ReqTest#no_path_param() ->");
      System.out.println(e.getMessage());
    }
  }
}
