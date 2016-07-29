package kaze.test.http.junit;

import org.junit.Test;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.tool.HttpReq;
import kaze.test.http.tool.HttpRes;

// before execute, run server.
public class ReqUriTest {

  @Http({"GET", "/cake/name/:name"})
  public void name(Req req, Res res) {
    res.json("name", req.uri(":name"));
  }
  @Test
  public void name() throws Exception {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/cake/name/cheese"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"name\":\"cheese\"}"
    );
  }

  
  @Http({"GET", "/cake/id/:id"})
  public void id(Req req, Res res) {
    res.json("id", req.uri(":id", Long.class));
  }
  @Test  //OK
  public void id() {
    HttpReq.get(
        "http://localhost:8080/cake/id/8"
    ).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8}"
    );
  }
  @Test  //NG
  public void badId() {
    HttpReq.get(
        "http://localhost:8080/cake/id/ng"
    ).statusIs(400).contentTypeIsJson().bodyContains(
        "\"cause\":\"convert\""
    );
  } 

  
  @Http({"POST", "/cake/id/:id/name/:name"})
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
		).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8,\"name\":\"cheese\"}"
    );
	}
}
