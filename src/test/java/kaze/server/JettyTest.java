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
    HttpRes res = HttpReq.get(
      "http://localhost:8080/index.html"
    );
    res.statusIs(200).typeIs("text/html").bodyIs(
      "<p>Index</p>"
    ).close();
  }
}
