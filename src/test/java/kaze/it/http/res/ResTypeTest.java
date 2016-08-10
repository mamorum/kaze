package kaze.it.http.res;

import org.junit.Test;

import kaze.Http;
import kaze.Http.Method;
import kaze.http.Req;
import kaze.http.Res;
import kaze.http.res.Type;
import kaze.it.http.tool.HttpReq;
import kaze.it.http.tool.ItCase;

public class ResTypeTest extends ItCase {

  private static final String HTML_OK = 
      "<html><body>OK</body></html>";
  
  private static final String HTML_NG = 
      "<html><body>NG</body></html>";
  
  @Http({Method.GET, "/res/type/ok"})
  public void ok(Req req, Res res) {
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
  
  @Http({Method.GET, "/res/type/ng"})
  public void ng(Req req, Res res) {
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
