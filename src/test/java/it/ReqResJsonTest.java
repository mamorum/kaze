package it;

import org.junit.BeforeClass;
import org.junit.Test;

import it.tool.Env;
import it.tool.HttpReq;
import it.tool.HttpRes;

public class ReqResJsonTest {

  static class Person {
    public long id;
    public String name;
    public long ver;
  }

  @BeforeClass public static void init() {
    reg_it_json_person();
    reg_it_json_msg();
    reg_it_json_msgs();
    Env.init();
  }

  //-> Test for Req#json(Class<T>), Res#json(Object)
  public static void reg_it_json_person() {
    Env.app.post.add("/it/json/person", (req, res) -> {
      Person p = req.json(Person.class);
      p.ver = 1;
      res.json(p);
    });
  }
  @Test public void it_json_person() {
    HttpRes res = HttpReq.postJson(
      "http://localhost:8080/app/it/json/person",
      "{\"id\":1,\"name\":\"Jone\"}"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
      "{\"id\":1,\"name\":\"Jone\",\"ver\":1}"
    ).close();
  }

  //-> Test for Res#json(Object...), size=2
  public static void reg_it_json_msg() {
    Env.app.get.add("/it/json/msg", (req, res) -> {
      res.json("msg", "Hello");
    });
  }
  @Test public void it_json_msg() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/json/msg"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
      "{\"msg\":\"Hello\"}"
    ).close();
  }

  //-> Test for Res#json(Object...), size=4
  public static void reg_it_json_msgs() {
    Env.app.get.add("/it/json/msgs", (req, res) -> {
      res.json(
        "status", 1,
        "history", new String[] {"stop", "start"}
      );
    });
  }
  @Test public void it_json_msgs() {
    HttpRes res = HttpReq.get(
      "http://localhost:8080/app/it/json/msgs"
    );
    res.statusIs(200).typeIsJsonUtf8().bodyIs(
      "{\"status\":1,\"history\":[\"stop\",\"start\"]}"
    ).close();
  }
}
