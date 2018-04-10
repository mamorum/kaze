package kaze;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import tools.JettyEnv;
import tools.HttpReq;
import tools.HttpRes;

public class ResJettyTest {
  @BeforeClass public static void init() {
    reg_it_status();
    reg_it_write();
    reg_it_html();
    reg_it_json_string();
    reg_it_json_object();
    reg_it_json_two_object();
    reg_it_json_four_object();
    JettyEnv.init();
  }
  //-> #status(int)
  public static void reg_it_status() {
    JettyEnv.app.get("/it/res/status", (req, res) -> {
      res.status(400);
    });
  }
  @Test public void it_status() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/status"
    );
    res.statusIs(400);
    res.close();
  }

  //-> #write(String, String)
  public static void reg_it_write() {
    JettyEnv.app.get("/it/res/write", (req, res) -> {
      res.write("text/plain", "Text.");
    });
  }
  @Test public void it_write() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/write"
    );
    res.statusIs(200);
    res.typeIs("text/plain;charset=utf-8");
    res.bodyIs("Text.");
    res.close();
  }

  //-> #html(String)
  public static void reg_it_html() {
    JettyEnv.app.get("/it/res/html", (req, res) -> {
      res.html("<p>Html.</p>");
    });
  }
  @Test public void it_html() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/html"
    );
    res.statusIs(200);
    res.typeIs("text/html;charset=utf-8");
    res.bodyIs("<p>Html.</p>");
    res.close();
  }

  //-> #json(String)
  public static void reg_it_json_string() {
    JettyEnv.app.get("/it/res/json/string", (req, res) -> {
      res.json("{\"id\":1}");
    });
  }
  @Test public void it_json_string() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/json/string"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs("{\"id\":1}");
    res.close();
  }
  //-> #json(Object)
  public static void reg_it_json_object() {
    JettyEnv.app.get("/it/res/json/object", (req, res) -> {
      res.json(Collections.singletonMap("msg", "Hello."));
    });
  }
  @Test public void it_json_object() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/json/object"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs("{\"msg\":\"Hello.\"}");
    res.close();
  }
  //-> #json(Object...)
  public static void reg_it_json_two_object() {
    JettyEnv.app.get("/it/res/json/2/object", (req, res) -> {
      res.json(
        "id", Integer.valueOf(1),
        "name", "Tom"
      );
    });
  }
  @Test public void it_json_two_object() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/json/2/object"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs("{\"id\":1,\"name\":\"Tom\"}");
    res.close();
  }
  public static void reg_it_json_four_object() {
    JettyEnv.app.get("/it/res/json/4/object", (req, res) -> {
      res.json(
        "id", Integer.valueOf(1),
        "name", "Tom",
        "zip", "111222",
        "tel", "111222333"
      );
    });
  }
  @Test public void it_json_four_object() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/res/json/4/object"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs(
      "{\"id\":1,\"name\":\"Tom\"," +
      "\"zip\":\"111222\",\"tel\":\"111222333\"}"
    );
    res.close();
  }
}
