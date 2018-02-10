package it;

import org.junit.BeforeClass;
import org.junit.Test;

import it.tool.Env;
import it.tool.HttpReq;
import it.tool.HttpRes;

public class ReqPathTest {
  @BeforeClass public static void init() {
    reg_it_path_name();
    Env.init();
  }

  //-> Test for Req#path(String)
  public static void reg_it_path_name() {
    Env.app.get("/it/req/path/:name", (req, res) -> {
      String name = req.path(":name");
      res.write("text/plain", name);
    });
  }
  @Test public void it_path_name() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/req/path/Jone"
    );
    res.statusIs(200).typeIs("text/plain;charset=utf-8").bodyIs(
      "Jone"
    ).close();
  }
}
