package kaze.test.http.junit;

import org.junit.Test;

import kaze.test.http.util.HttpReq;
import kaze.test.http.util.HttpRes;

public class IndexTest {

  @Test public void testHello() throws Exception {
    
    // do
    HttpRes res = HttpReq.get("http://localhost:8080/");
    
    // check
    res.statusIs(200).contentTypeIsJson().bodyIs(
        "{\"msg\":\"Hello!\"}"
    );
  }
}
