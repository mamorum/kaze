package kaze.it.http.junit;

import org.junit.Test;

import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResJsonTest extends ItCase {

  @Test  // for kaze.test.http.Main#hello
  public void hello() throws Exception {
    HttpReq.get(
        "http://localhost:8080/hello"
    ).statusIs(200).contentTypeIsJson().bodyIs(
        "{\"msg\":\"Hello!\"}"
    );
  }
}
