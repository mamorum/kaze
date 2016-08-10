package kaze.it.http.req;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.HttpRes;
import kaze.it.http.tool.ItCase;

// before execute, run server.
public class ReqUriTest extends ItCase {

  final String uri = "/cake";
  
  @Http({Method.GET, uri + "/name/:name"})
  public void name(Req req, Res res) {
    res.json("name", req.uri(":name"));
  }
  @Test
  public void name() {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/cake/name/cheese"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"name\":\"cheese\"}"
    ).close();
  }

  
  @Http({Method.GET, uri + "/id/:id"})
  public void id(Req req, Res res) {
    res.json("id", req.uri(":id", Long.class));
  }
  @Test  //OK
  public void id() {
    HttpReq.get(
        "http://localhost:8080/cake/id/8"
    ).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":8}"
    ).close();
  }
  @Test  //NG
  public void badId() {
    HttpReq.get(
        "http://localhost:8080/cake/id/ng"
    ).statusIs(400).typeIsJsonUtf8().bodyContains(
        "\"cause\":\"convert\""
    ).close();
  } 

  
  @Http({Method.POST, uri + "/id/:id/name/:name"})
  public void idName(Req req, Res res) {
    res.json(
        "id", req.uri(":id", Long.class),
        "name", req.uri(":name")
    );
  }
	@Test
	public void idName() {
	  HttpReq.postParams(
		    "http://localhost:8080/cake/id/8/name/cheese",
		    ""
		).statusIs(200).typeIsJsonUtf8().bodyIs(
        "{\"id\":8,\"name\":\"cheese\"}"
    ).close();
	}
}
