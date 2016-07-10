package it.http;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.WebApi;
import kaze.http.method.Get;

@WebApi
public class PersonApi {
	
	@Get("/person") public void param(Req req, Res res) {
		String id = req.param.val("id");
		res.json("id", id);
	}

	// TODO change http method to Post.
	@Get("/person/params") public void params(Req req, Res res) {
		Person p = req.param.bind(Person.class).get();
		res.json(p);
	}
	
	// TODO change http method to Post.
	@Get("/person/json") public void jsonBody(Req req, Res res) {
		Person p = req.json.bind(Person.class).get();
		res.json(p);
	}
	
	@Get("/html") public void html(Req req, Res res) {
		res.contentType(
			"text/html;charset=utf-8"
		).write(
			"<html><body><p>Hello !</p><body></html>"
		);
	}

}
