package kaze;

import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import kaze.server.Jetty;
import tools.JettyThread;
import tools.HttpReq;
import tools.HttpRes;

public class ResJettyTest {
  private static final
    App app=new App(), ngApp=new App();
  @BeforeClass public static void init() {
    Gson gson = new Gson();
    app.conv(null, gson::toJson);
    Jetty.app(app, "/res/*");
    Jetty.app(ngApp, "/res/ng/*");
    JettyThread.start();
  }
  /* OK */
  //-> #status(int)
  @Test public void status() {
    app.get("/status", (req, res) -> {
      res.status(400);
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/status"
    );
    res.statusIs(400);
    res.close();
  }

  //-> #write(String, String)
  @Test public void write() {
    app.get("/write", (req, res) -> {
      res.write("text/plain", "Text.");
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/write"
    );
    res.statusIs(200);
    res.typeIs("text/plain;charset=utf-8");
    res.bodyIs("Text.");
    res.close();
  }

  //-> #html(String)
  @Test public void html() {
    app.get("/html", (req, res) -> {
      res.html("<p>Html.</p>");
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/html"
    );
    res.statusIs(200);
    res.typeIs("text/html;charset=utf-8");
    res.bodyIs("<p>Html.</p>");
    res.close();
  }

  //-> #json(String)
  @Test public void json_string() {
    app.get("/json/string", (req, res) -> {
      res.json("{\"id\":1}");
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/json/string"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs("{\"id\":1}");
    res.close();
  }
  //-> #json(Object)
  @Test public void json_object() {
    app.get("/json/object", (req, res) -> {
      res.json(Collections.singletonMap("msg", "Hello."));
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/json/object"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs("{\"msg\":\"Hello.\"}");
    res.close();
  }
  //-> #json(Object...)
  @Test public void json_two_object() {
    app.get("/json/2/object", (req, res) -> {
      res.json(
        "id", Integer.valueOf(1),
        "name", "Tom"
      );
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/json/2/object"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs("{\"id\":1,\"name\":\"Tom\"}");
    res.close();
  }
  @Test public void json_four_object() {
    app.get("/json/4/object", (req, res) -> {
      res.json(
        "id", Integer.valueOf(1),
        "name", "Tom",
        "zip", "111222",
        "tel", "111222333"
      );
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/json/4/object"
    );
    res.statusIs(200);
    res.typeIsJsonUtf8();
    res.bodyIs(
      "{\"id\":1,\"name\":\"Tom\"," +
      "\"zip\":\"111222\",\"tel\":\"111222333\"}"
    );
    res.close();
  }

  /* NG */
  //-> #json(Object)
  @Test public void ng_no_json_parser_on_res() {
    ngApp.get("/json", (req, res) -> {
      try {
        res.json(
          Collections.singletonMap("msg", "Hello.")
        );
        fail();
      } catch (IllegalStateException e) {
        System.out.println("ng_no_json_parser_on_res->");
        throw e; // expected
      } catch (Exception e) {
        fail();
      }
    });
    HttpRes res = HttpReq.get(
      "http://localhost:8080/res/ng/json"
    );
    res.statusIs(500);
    res.close();
  }
}