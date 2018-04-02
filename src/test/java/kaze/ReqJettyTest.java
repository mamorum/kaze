package kaze;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.JettyEnv;
import tools.HttpReq;
import tools.HttpRes;

public class ReqJettyTest {
  @BeforeClass public static void init() {
    reg_it_param();
    reg_it_path();
    reg_it_body();
    reg_it_json();
    JettyEnv.init();
  }
  //-> #param(String)
  public static void reg_it_param() {
    JettyEnv.app.get("/it/req/param", (req, res) -> {
      String name = req.param("name");
      assertEquals("Jone", name);
    });
  }
  @Test public void it_param() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/req/param?name=Jone"
    );
    res.statusIs(200).close();
  }
  //-> #path(String)
  public static void reg_it_path() {
    JettyEnv.app.get("/it/req/path/:name", (req, res) -> {
      String name = req.path(":name");
      assertEquals("Jone", name);
    });
  }
  @Test public void it_path() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/req/path/Jone"
    );
    res.statusIs(200).close();
  }
  //-> #body()
  public static void reg_it_body() {
    JettyEnv.app.post("/it/req/body", (req, res) -> {
      String b = req.body();
      assertEquals("{\"name\":\"Jone\"}", b);
    });
  }
  @Test public void it_body() {
    HttpRes res = HttpReq.postJson(
      "http://localhost:8080/app/it/req/body",
      "{\"name\":\"Jone\"}"
    );
    res.statusIs(200).close();
  }
  //-> #json(Class)
  static class Person {
    long id; String name;
  }
  public static void reg_it_json() {
    JettyEnv.app.post("/it/req/json", (req, res) -> {
      Person p = req.json(Person.class);
      assertEquals(1, p.id);
      assertEquals("Jone", p.name);
    });
  }
  @Test public void it_json() {
    HttpRes res = HttpReq.postJson(
      "http://localhost:8080/app/it/req/json",
      "{\"id\":1,\"name\":\"Jone\"}"
    );
    res.statusIs(200).close();
  }
}
