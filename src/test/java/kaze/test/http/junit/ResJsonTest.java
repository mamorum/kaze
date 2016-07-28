package kaze.test.http.junit;

import org.junit.Test;

import kaze.test.http.tool.HttpReq;

public class ResJsonTest {

  @Test  // for kaze.test.http.Main#hello
  public void hello() throws Exception {
    HttpReq.get(
        "http://localhost:8080/hello"
    ).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"msg\":\"Hello!\"}"
    );
  }
}
