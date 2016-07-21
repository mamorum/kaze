package kaze.test.http.api;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class ReqUriApi {

  @Http({"GET", "/uri/name/:name"})
  public void name(Req req, Res res) {
    res.json("name", req.uri(":name"));
  }
  
  @Http({"GET", "/uri/id/:id"})
  public void id(Req req, Res res) {
    res.json("id", req.uri(":id", Long.class));
  }
  
  @Http({"POST", "/uri/:id/uri/:name"})
  public void idName(Req req, Res res) {
    res.json(
        "id", req.uri(":id", Long.class),
        "name", req.uri(":name")
    );
  }
}
