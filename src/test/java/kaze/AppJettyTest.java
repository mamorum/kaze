package kaze;

import org.junit.BeforeClass;
import org.junit.Test;

import kaze.opt.Jetty;
import tools.HttpReq;
import tools.HttpRes;
import tools.JettyThread;

public class AppJettyTest {
  static final App app = new App();
  @BeforeClass public static void init() {
    Jetty.app(app , "/app/ng/*");
    JettyThread.start();
  }
  /* NG */
  //-> run
  @Test public void run_not_found() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/ng/nf"
    );
    res.statusIs(404);
    res.close();
  }
}