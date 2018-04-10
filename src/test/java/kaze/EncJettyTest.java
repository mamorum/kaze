package kaze;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlet.FilterHolder;
import org.junit.BeforeClass;
import org.junit.Test;

import kaze.server.Jetty;
import tools.HttpReq;
import tools.HttpRes;
import tools.JettyThread;

public class EncJettyTest {
  @BeforeClass public static void init() {
    JettyThread.start();
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
  @Test public void enc_filter() {
    // pre
    App app = new App();
    app.disableEncoding();
    app.get("/on", (req, res) -> {
      res.write("text/plain", "on");
    });
    app.get("/off", (req, res) -> {
      res.write("text/plain", "off");
    });
    Jetty.app(app , "/enc-filter/*");
    FilterHolder f = new FilterHolder(new Enc());
    f.setInitParameter("encoding", "utf-8");
    Jetty.context.addFilter(
      f, "/enc-filter/on", EnumSet.of(DispatcherType.REQUEST)
    );
    HttpRes res;
    // on -> test, check
    res = HttpReq.get(
      "http://localhost:8080/enc-filter/on"
    );
    res.typeIs("text/plain;charset=utf-8");
    res.statusIs(200);
    res.bodyIs("on");
    res.close();
    // off -> test, check
    res = HttpReq.get(
      "http://localhost:8080/enc-filter/off"
    );
    res.typeIs("text/plain;charset=iso-8859-1");
    res.statusIs(200);
    res.bodyIs("off");
    res.close();
  }
}
