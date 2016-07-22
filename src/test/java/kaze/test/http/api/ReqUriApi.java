package kaze.test.http.api;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class ReqUriApi {
  
  @Http({"GET", "/cake/id/:id"})
  public void id(Req req, Res res) {
    res.json("id", req.uri(":id", Long.class));
  }

  @Http({"GET", "/cake/name/:name"})
  public void name(Req req, Res res) {
    res.json("name", req.uri(":name"));
  }
  
  @Http({"POST", "/cake/id/:id/name/:name"})
  public void idName(Req req, Res res) {
    res.json(
        "id", req.uri(":id", Long.class),
        "name", req.uri(":name")
    );
  }
}
