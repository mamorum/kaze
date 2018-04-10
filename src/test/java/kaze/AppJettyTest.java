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
  //-> encoding test
  @Test public void enc_enable() {
    // pre
    App app = new App();
    app.get("/", (req, res) -> {
      res.write("text/plain", "Hi");
    });
    Jetty.app(app , "/enc-enable/*");
    // test
    HttpRes res = HttpReq.get(
      "http://localhost:8080/enc-enable"
    );
    // check
    res.typeIs("text/plain;charset=utf-8");
    res.statusIs(200);
    res.bodyIs("Hi");
    res.close();
  }
  @Test public void enc_disable() {
    // pre
    App app = new App();
    app.disableEncoding();
    app.get("/", (req, res) -> {
      res.write("text/plain", "Hi");
    });
    Jetty.app(app , "/enc-disable/*");
    // test
    HttpRes res = HttpReq.get(
      "http://localhost:8080/enc-disable"
    );
    // check
    /// iso-... is jetty's default charset for text/plain.
    res.typeIs("text/plain;charset=iso-8859-1");
    res.statusIs(200);
    res.bodyIs("Hi");
    res.close();
  }
  @Test public void enc_changed() {
    // pre
    App app = new App();
    app.encoding("utf-16");
    app.get("/", (req, res) -> {
      res.write("text/plain", "Hi");
    });
    Jetty.app(app , "/enc-changed/*");
    // test
    HttpRes res = HttpReq.get(
      "http://localhost:8080/enc-changed"
    );
    // check
    res.typeIs("text/plain;charset=utf-16");
    res.statusIs(200);
    res.bodyIs("Hi");
    res.close();
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
