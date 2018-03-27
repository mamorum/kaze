package kaze;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RouteTest {
  App app;
  @Before public void init() {
    app = new App();
  }
  private void msg(String method, Exception e) {
    System.out.print("RouteTest#");
    System.out.println(method);
    System.out.println(e.getMessage());
  }
  //-> root path
  @Test public void duplicate_root_path() {
    app.get("/", null);
    try {
      app.get("/", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_root_path() ->", e);
    }
    try {
      app.get("", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_root_path() ->", e);
    }
  }
  //-> static path
  @Test public void duplicate_static_path_11() {
    app.post("/cat", null);
    try {
      app.post("/cat", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_static_path_11() ->", e);
    }
  }
  @Test public void duplicate_static_path_21() {
    app.put("/cat/mike", null);
    try {
      app.put("/cat/mike", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_static_path_21() ->", e);
    }
  }
  //-> dynamic path
  @Test public void duplicate_dynamic_path_11() {
    app.delete("/cat", null);
    try {
      app.delete("/:animal", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_11() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_12() {
    app.put("/:animal", null);
    try {
      app.put("/cat", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_12() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_21() {
    app.delete("/cat/bob", null);
    try {
      app.delete("/cat/:name", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_21() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_22() {
    app.get("/cat/:name", null);
    try {
      app.get("/cat/bob", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_22() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_31() {
    app.get("/animal/cat/bob", null);
    try {
      app.get("/animal/:category/bob", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_21() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_32() {
    app.get("/animal/:category/bob", null);
    try {
      app.get("/animal/cat/bob", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_22() ->", e);
    }
  }
}
