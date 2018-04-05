package kaze;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RoutesTest {
  Routes routes;
  @Before public void init() {
    routes = new Routes();
  }
  private void msg(String method, Exception e) {
    System.out.print("RoutesTest#");
    System.out.println(method);
    System.out.println(e.getMessage());
    //e.printStackTrace();
  }
  //-> Tests for Routes#add()
  //-> root path
  @Test public void duplicate_root_path() {
    routes.add("/", null);
    try {
      routes.add("/", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_root_path() ->", e);
    }
    try {
      routes.add("", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_root_path() ->", e);
    }
  }
  //-> static path
  @Test public void duplicate_static_path_11() {
    routes.add("/cat", null);
    try {
      routes.add("/cat", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_static_path_11() ->", e);
    }
  }
  @Test public void duplicate_static_path_21() {
    routes.add("/cat/mike", null);
    try {
      routes.add("/cat/mike", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_static_path_21() ->", e);
    }
  }
  //-> dynamic path
  @Test public void duplicate_dynamic_path_11() {
    routes.add("/cat", null);
    try {
      routes.add("/:animal", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_11() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_12() {
    routes.add("/:animal", null);
    try {
      routes.add("/cat", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_12() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_21() {
    routes.add("/cat/bob", null);
    try {
      routes.add("/cat/:name", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_21() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_22() {
    routes.add("/cat/:name", null);
    try {
      routes.add("/cat/bob", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_22() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_31() {
    routes.add("/animal/cat/bob", null);
    try {
      routes.add("/animal/:category/bob", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_21() ->", e);
    }
  }
  @Test public void duplicate_dynamic_path_32() {
    routes.add("/animal/:category/bob", null);
    try {
      routes.add("/animal/cat/bob", null);
      fail();
    } catch (IllegalStateException e) {  //-> OK
      msg("duplicate_dynamic_path_22() ->", e);
    }
  }
}
