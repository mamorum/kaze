package it.http;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.Route;

public class PersonApi {
	
	@Route({"GET", "/person/:id"})
	public void uri(Req req, Res res) {
		String id = req.uri.val(":id");
		res.json("id", id);
	}
	
	@Route({"GET", "/person"})
	public void param(Req req, Res res) {
		String id = req.param("id");
		res.json("id", id);
	}
	
	@Route({"POST", "/person/params"})
	public void params(Req req, Res res) {
		Person p = req.params().bind(Person.class).get();
		res.json(p);
	}
	
	@Route({"POST", "/person/json"})
	public void jsonBody(Req req, Res res) {
		Person p = req.json().bind(Person.class).get();
		res.json(p);
	}
	
	@Route({"GET", "/html"})
	public void html(Req req, Res res) {
		res.contentType(
			"text/html;charset=utf-8"
		).write(
			"<html><body><p>Hello !</p><body></html>"
		);
	}

}
