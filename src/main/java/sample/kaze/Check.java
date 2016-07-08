package sample.kaze;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.WebApi;
import kaze.http.method.Get;

@WebApi
public class Check {

	@Get("/search") public void search(Req req, Res res) {
		String word = req.param("word");
		res.json("word", word);
	}
	
	@Get("/echo") public void echo(Req req, Res res) {
		Msg msg = req.json(Msg.class);
		res.json(msg);
	}
	
	@Get("/html") public void html(Req req, Res res) {
		res.contentType(
			"text/html;charset=utf-8"
		).write(
			"<html><body><p>Hello !</p><body></html>"
		);
	}
	 
}
