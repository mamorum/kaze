package kaze.test.http.api;

import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;
import kaze.test.http.model.Person;

public class PersonApi {
	
	@Http({"GET", "/person"})
	public void param(Req req, Res res) {
		String id = req.param("id");
		res.json("id", id);
	}
	
	@Http({"POST", "/person/params"})
	public void params(Req req, Res res) {
		Person p = req.params().bind(Person.class).get();
		res.json(p);
	}
	
	@Http({"POST", "/person/json"})
	public void jsonBody(Req req, Res res) {
		Person p = req.json().bind(Person.class).get();
		res.json(p);
	}
}
