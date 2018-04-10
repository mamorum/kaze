package kaze.server;

import org.junit.BeforeClass;
import org.junit.Test;

import tools.JettyEnv;
import tools.HttpReq;
import tools.HttpRes;

public class JettyTest {
  @BeforeClass public static void init() {
    JettyEnv.init();
  }
  @Test public void static_file() {
    // test
    HttpRes res = HttpReq.get(
      "http://localhost:8080/index.html"
    );
    // check
    res.statusIs(200);
    res.typeIs("text/html");
    res.bodyIs("<p>Index</p>");
    res.close();
  }
}
