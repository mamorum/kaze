package kaze.test.http.api;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.Route;

public class ReqUriApi {

  @Route({"GET", "/uri/name/:name"})
  public void name(Req req, Res res) {
    res.json("name", req.uri().path(":name"));
  }
  
  @Route({"GET", "/uri/id/:id"})
  public void id(Req req, Res res) {
    res.json("id", req.uri().path(":id", Long.class));
  }
  
  @Route({"POST", "/uri/:id/uri/:name"})
  public void idName(Req req, Res res) {
    res.json(
        "id", req.uri().path(":id", Long.class),
        "name", req.uri().path(":name")
    );
  }
}
