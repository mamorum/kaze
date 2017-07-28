package it;

import org.junit.BeforeClass;
import org.junit.Test;

import it.tool.HttpReq;
import it.tool.HttpRes;
import it.tool.ItCase;

public class ReqParamTest extends ItCase {

  @BeforeClass public static void init() {
    reg_it_param();
  }

  //-> Test for Req#param(String)
  public static void reg_it_param() {
    app.get("/it/req/param", (req, res) -> {
      String name = req.param("name");
      res.send(name);
    });
  }
  @Test public void it_param() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/it/req/param?name=Jone"
    );
    res.statusIs(200).typeIs("text/plain;charset=utf-8").bodyIs(
      "Jone"
    ).close();
  }
}
