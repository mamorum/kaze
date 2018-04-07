package kaze;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResTest {
  @Test public void no_json_parser() {
    Res res = new Res(null, null);
    try {
      res.json(new Object());
      fail();
    } catch (IllegalStateException e) {  //-> OK
      System.out.println("ResTest#no_json_parser() ->");
      System.out.println(e.getMessage());
    }
  }
}
