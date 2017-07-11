package it.http.res;

import org.junit.Before;
import org.junit.Test;

import it.http.tool.HttpReq;
import it.http.tool.ItCase;
import kaze.Req;
import kaze.Res;
import kaze.Res.Type;

public class ResTypeTest extends ItCase {

  @Before public void regist() {
    http.get("/res/type/ok", ResTypeTest::ok);
    http.get("/res/type/ng", ResTypeTest::ng);
  }

  private static final String HTML_OK = 
      "<html><body>OK</body></html>";
  
  private static final String HTML_NG = 
      "<html><body>NG</body></html>";

  public static void ok(Req req, Res res) {
    res.type(Type.HTML).write(
      HTML_OK
    );
  }
  @Test
  public void ok() {
    HttpReq.get(
        "http://localhost:8080/res/type/ok"
    ).statusIs(200).typeIs(
        "text/html; charset=UTF-8"
    ).bodyIs(HTML_OK).close();
  }


  public static void ng(Req req, Res res) {
    res.status(400).type(Type.HTML).write(
      HTML_NG
    );
  }
  @Test
  public void ng() {
    HttpReq.get(
        "http://localhost:8080/res/type/ng"
    ).statusIs(400).typeIs(
        "text/html; charset=UTF-8"
    ).bodyIs(HTML_NG).close();
  }
}
