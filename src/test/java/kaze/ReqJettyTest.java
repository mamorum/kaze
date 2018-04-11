package kaze;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import kaze.server.Jetty;
import tools.HttpReq;
import tools.HttpRes;
import tools.JettyThread;

public class ReqJettyTest {
  private static final
    App app=new App(), ngApp=new App();
  @BeforeClass public static void init() {
    Gson gson = new Gson();
    app.conv(gson::fromJson, null);
    Jetty.app(app, "/req/*");
    Jetty.app(ngApp, "/req/ng/*");
    JettyThread.start();
  }
  //-> #param(String)
  @Test public void param() {
    app.get("/param", (req, res) -> {
      String name = req.param("name");
      assertEquals("Jone", name);
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/req/param?name=Jone"
    );
    res.statusIs(200);
    res.close();
  }
  //-> #path(String)
  @Test public void path() {
    app.get("/path/:name", (req, res) -> {
      String name = req.path(":name");
      assertEquals("Jone", name);
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/req/path/Jone"
    );
    res.statusIs(200);
    res.close();
  }
  //-> #body()
  @Test public void body() {
    app.post("/body", (req, res) -> {
      String b = req.body();
      assertEquals("{\"name\":\"Jone\"}", b);
    });
    HttpRes res = HttpReq.postJson(
      "http://localhost:8080/req/body",
      "{\"name\":\"Jone\"}"
    );
    res.statusIs(200);
    res.close();
  }
  //-> #json(Class)
  static class Person {
    long id; String name;
  }
  @Test public void json() {
    app.post("/json", (req, res) -> {
      Person p = req.json(Person.class);
      assertEquals(1, p.id);
      assertEquals("Jone", p.name);
    });
    HttpRes res = HttpReq.postJson(
      "http://localhost:8080/req/json",
      "{\"id\":1,\"name\":\"Jone\"}"
    );
    res.statusIs(200);
    res.close();
  }

  /* NG */
  @Test public void ng_no_json_parser_on_req() {
    ngApp.get("/json", (req, res) -> {
      try {
        req.json(Object.class);
        fail();
      } catch (IllegalStateException e) {
        System.out.println("ng_no_json_parser_on_req->");
        throw e; // expected
      } catch (Exception e) {
        fail();
      }
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/req/ng/json"
    );
    res.statusIs(500);
    res.close();
  }
  @Test public void ng_path_param_not_found() {
    ngApp.get("/cat/:name", (req, res) -> {
      try {
        req.path(":param");
        fail();
      } catch (IllegalArgumentException e) {
        System.out.println("ng_path_param_not_found->");
        throw e; // expected
      } catch (Exception e) {
        fail();
      }
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/req/ng/cat/mike"
    );
    res.statusIs(500);
    res.close();
  }
  @Test public void ng_path_param_not_defined() {
    ngApp.get("/dog/tom", (req, res) -> {
      try {
        req.path(":param");
        fail();
      } catch (IllegalStateException e) {
        System.out.println("ng_path_param_not_defined->");
        throw e; // expected
      } catch (Exception e) {
        fail();
      }
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/req/ng/dog/tom"
    );
    res.statusIs(500);
    res.close();
  }
}
