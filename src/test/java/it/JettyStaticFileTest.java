package it;

import org.junit.BeforeClass;
import org.junit.Test;

import it.tool.Env;
import it.tool.HttpReq;
import it.tool.HttpRes;

public class JettyStaticFileTest {
  @BeforeClass public static void init() {
    Env.init();
  }
  @Test public void index() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/index.html"
    );
    res.statusIs(200).typeIs("text/html").bodyIs(
      "<p>Index</p>"
    ).close();
  }

}
