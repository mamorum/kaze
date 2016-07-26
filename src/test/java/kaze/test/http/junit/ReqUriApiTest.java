package kaze.test.http.junit;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.google.api.client.http.HttpResponseException;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

// before execute, run server.
public class ReqUriApiTest {

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
  public void id() throws Exception {
    HttpRes res = HttpReq.get(
        "http://localhost:8080/cake/id/8"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8}"
    );
  }
  @Test  //NG
  public void badId() throws Exception {
    try {
      HttpReq.get(
          "http://localhost:8080/cake/id/ng"
      );
    } catch (HttpResponseException e) {
      assertThat(e.getStatusCode()).isEqualTo(400);
      assertThat(e.getHeaders().getContentType()).isEqualTo("application/json;charset=utf-8");
      System.out.println(e.getContent());
    }
  } 

  
  @Http({"POST", "/cake/id/:id/name/:name"})
  public void idName(Req req, Res res) {
    res.json(
        "id", req.uri(":id", Long.class),
        "name", req.uri(":name")
    );
  }
	@Test
	public void idName() throws Exception {
	  HttpRes res = HttpReq.postParams(
		    "http://localhost:8080/cake/id/8/name/cheese",
		    ""
		);
		res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8,\"name\":\"cheese\"}"
    );
	}
}
