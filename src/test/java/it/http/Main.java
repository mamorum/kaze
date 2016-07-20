package it.http;

import kaze.App;
import kaze.http.Req;
import kaze.http.Res;
import kaze.http.Route;

public class Main {

  @Route({"GET", "/hello"})
  public void hello(Req req, Res res) {
    res.json("msg", "Hello!");
  }
  
	public static void main(String[] args) {
		App.start("it.http");
	}

}
