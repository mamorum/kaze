package it;

import org.junit.BeforeClass;
import org.junit.Test;

import it.tool.HttpReq;
import it.tool.HttpRes;
import it.tool.Env;
import kaze.App;

public class ReqParamTest {
  @BeforeClass public static void init() {
    reg_it_param();
    Env.init();
  }

  //-> Test for Req#param(String)
  public static void reg_it_param() {
    App.get("/it/req/param", (req, res) -> {
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
