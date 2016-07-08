package sample.kaze;

import kaze.App;
import kaze.http.Req;
import kaze.http.Res;
import kaze.http.WebApi;
import kaze.http.method.Get;

@WebApi
public class Main {
	
	@Get("/hello") public void hello(Req req, Res res) {
		res.json("msg", "Hello!");
	}
	 
	public static void main(String[] args) {
		App.start("sample.kaze");
	}
}
