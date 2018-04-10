package kaze;

import org.junit.BeforeClass;
import org.junit.Test;

import kaze.server.Jetty;
import tools.HttpReq;
import tools.HttpRes;
import tools.JettyThread;

public class AppJettyTest {
  @BeforeClass public static void init() {
    JettyThread.start();
  }
  //-> run test
  @Test public void run_not_found() {
    App app = new App();
    Jetty.app(app , "/nf/*");
    HttpRes res = HttpReq.get(
      "http://localhost:8080/nf/hello"
    );
    res.statusIs(404);
    res.close();
  }
}
