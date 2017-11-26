package it;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.tool.Env;
import it.tool.HttpReq;
import it.tool.HttpRes;
import kaze.AppUtil;

public class NoJsonParserTest {
  @BeforeClass public static void init() {
    reg_it_req_json();
    reg_it_res_json();
    Env.init();
    AppUtil.noJsonParser(Env.app);
  }
  @AfterClass public static void end() {
    Env.initJsonParser();
  }
  public static void reg_it_req_json() {
    Env.app.get("/nojson/req", (req, res) -> {
      try { req.json(String.class); }
      catch (IllegalStateException e) {
        System.out.println("Req#json: No Parser ->");
        System.out.println(e);
      }
      res.status(501);
    });
  }
  @Test public void it_req_json_no_parser() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/nojson/req"
    );
    res.statusIs(501).close();
  }
  public static void reg_it_res_json() {
    Env.app.get("/nojson/res", (req, res) -> {
      try { res.json("msg", "nojson"); }
      catch (IllegalStateException e) {
        System.out.println("Res#json: No Parser ->");
        System.out.println(e);
      }
      res.status(501);
    });
  }
  @Test public void it_res_json_no_parser() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/nojson/res"
    );
    res.statusIs(501).close();
  }
}
