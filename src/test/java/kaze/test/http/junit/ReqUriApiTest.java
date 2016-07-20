package kaze.test.http.junit;

import org.junit.Test;

import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

// before execute, run it.http.Main.
//TODO Run App from JUnit testcase.
public class ReqUriApiTest {

  HttpRes res;
	 
	@Test public void name() throws Exception {
		
		// do
		res = HttpReq.get(
		    "http://localhost:8080/uri/name/bob"
		);
        
		// check
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"name\":\"bob\"}"
    );
	}
	
	@Test public void id() throws Exception {

    // do
    res = HttpReq.get(
        "http://localhost:8080/uri/id/8"
    );
        
    // check
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":8}"
    );
	}
	
	@Test public void idName() throws Exception {
		
		// do
		res = HttpReq.postParams(
		    "http://localhost:8080/uri/7/uri/tom", ""
		);
		
		// check
		res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":7,\"name\":\"tom\"}"
    );
	}
}
