package kaze.test.http.junit;

import org.junit.Test;

import kaze.test.http.util.HttpReq;

public class IndexTest {

  @Test  // for kaze.test.http.Main#index
  public void index() throws Exception {
    HttpReq.get(
        "http://localhost:8080/"
    ).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"msg\":\"Hello!\"}"
    );
  }
}
