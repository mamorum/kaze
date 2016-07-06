package sample.kaze;

import kaze.App;
import kaze.http.Req;
import kaze.http.Res;
import kaze.http.WebApi;
import kaze.http.method.Get;

@WebApi
public class Main {
	
	public @Get("/hello") void hello(Req req, Res res) {
		res.jsonFom("msg", "Hello!");
	}
	 
	public static void main(String[] args) {
		App.build("sample.kaze").start();
	}
}
