package kaze;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import kaze.server.Jetty;
import tools.JettyThread;
import tools.HttpReq;
import tools.HttpRes;

public class ReqJettyTest {
  private static final App app = new App();
  @BeforeClass public static void init() {
    Jetty.app(app, "/req/*");
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
    Gson gson = new Gson();
    app.conv(gson::fromJson, null);
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
}
