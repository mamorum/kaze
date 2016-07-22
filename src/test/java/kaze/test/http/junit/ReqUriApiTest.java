package kaze.test.http.junit;

import org.junit.Test;

import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

// before execute, run it.http.Main.
//TODO Run App from JUnit testcase.
public class ReqUriApiTest {

  HttpRes res;

  @Test public void id() throws Exception {
    res = HttpReq.get(
        "http://localhost:8080/cake/id/8"
    );
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8}"
    );
  }
  
	@Test public void name() throws Exception {
		res = HttpReq.get(
		    "http://localhost:8080/cake/name/cheese"
		);
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"name\":\"cheese\"}"
    );
	}
	
	@Test public void idName() throws Exception {
		res = HttpReq.postParams(
		    "http://localhost:8080/cake/id/8/name/cheese", ""
		);
		res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8,\"name\":\"cheese\"}"
    );
	}
}
