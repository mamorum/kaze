package kaze.server;

import org.junit.BeforeClass;
import org.junit.Test;

import tools.JettyThread;
import tools.HttpReq;
import tools.HttpRes;

public class JettyTest {
  @BeforeClass public static void init() {
    Jetty.doc("/public", "/");
    JettyThread.start();
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
