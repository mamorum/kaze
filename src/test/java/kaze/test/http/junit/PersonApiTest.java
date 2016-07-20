package kaze.test.http.junit;

import org.junit.Test;

import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

// before execute, run it.http.Main.
//TODO Run App from JUnit testcase.
public class PersonApiTest {

  HttpRes res;
	 
	@Test public void requestParam() throws Exception {
		
		// do
		res = HttpReq.get(
		    "http://localhost:8080/person?id=123"
		);
        
		// check
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"id\":\"123\"}"
    );
	}
	
	@Test public void requestParams() throws Exception {
		
		// do
		res = HttpReq.postParams(
		    "http://localhost:8080/person/params",
				"id=1234&name=Tom&langs=Perl&langs=PHP"
		);
        
		// check
		res.statusIs(200).contentTypeIsJson().bodyIs(
		    "{\"id\":1234,\"name\":\"Tom\",\"langs\":[\"Perl\",\"PHP\"]}"
    );
	}
	
	@Test public void requestJson() throws Exception {
		
	  // data
	  String json = 
	    "{\"id\":12345,\"name\":\"Bob\",\"langs\":[\"C\",\"Java\",\"JS\"]}";
	  
		// do
		res = HttpReq.postJson(
		    "http://localhost:8080/person/json", json
		);
		
		// check
		res.statusIs(200).contentTypeIsJson().bodyIs(json);
	}
}
