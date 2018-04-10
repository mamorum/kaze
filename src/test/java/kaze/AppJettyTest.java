package kaze;

import org.junit.BeforeClass;
import org.junit.Test;

import kaze.server.Jetty;
import tools.HttpReq;
import tools.HttpRes;
import tools.JettyEnv;

public class AppJettyTest {
  @BeforeClass public static void init() {
    JettyEnv.init();
  }
  //-> run test
  @Test public void run_not_found() {
    // pre
    App app = new App();
    Jetty.app(app , "/nf/*");
    // test
    HttpRes res = HttpReq.get(
      "http://localhost:8080/nf/hello"
    );
    // check
    res.statusIs(404);
    res.close();
  }
}
