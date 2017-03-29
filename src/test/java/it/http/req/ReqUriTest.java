package it.http.req;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.HttpRes;
import it.http.tool.ItCase;
import kaze.http.Req;
import kaze.http.Res;

// before execute, run server.
public class ReqUriTest extends ItCase {

  @Before public void regist() {
    http.get("/cake/name/:name", ReqUriTest::name);
    http.get("/cake/id/:id", ReqUriTest::id);
    http.post("/cake/id/:id/name/:name", ReqUriTest::idName);
  }

  // Test target
  public static void name(Req req, Res res) {
    res.json("name", req.path(":name"));
  }
  @Test public void name() {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/cake/name/cheese"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"name\":\"cheese\"}"
    ).close();
  }
  
  // Test target
  public static void id(Req req, Res res) {
    res.json("id", req.path(":id", Long.class));
  }
  @Test public void id() {  // OK
    HttpReq.get(
        "http://localhost:8080/cake/id/8"
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":8}"
    ).close();
  }
  @Test public void badId() {  // NG
    HttpReq.get(
        "http://localhost:8080/cake/id/ng"
    ).statusIs(400).typeIsJsonUtf8().bodyContains(
        "\"cause\":\"convert\""
    ).close();
  } 

  // Test target.
  public static void idName(Req req, Res res) {
    res.json(
        "id", req.path(":id", Long.class),
        "name", req.path(":name")
    );
  }
	@Test public void idName() {
	  HttpReq.postParams(
		    "http://localhost:8080/cake/id/8/name/cheese",
		    ""
		).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":8,\"name\":\"cheese\"}"
    ).close();
	}
}
