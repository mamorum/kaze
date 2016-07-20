package it.http;

import org.junit.Test;

import it.http.util.HttpReq;
import it.http.util.HttpRes;

public class MainTest {

  @Test public void testHello() throws Exception {
    
    // do
    HttpRes res = HttpReq.get("http://localhost:8080/hello");
    
    // check
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"msg\":\"Hello!\"}"
    );
  }
}
