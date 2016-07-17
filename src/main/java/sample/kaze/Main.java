package sample.kaze;

import kaze.App;
import kaze.http.Route;
import kaze.http.Req;
import kaze.http.Res;

public class Main {
	
	@Route({"GET", "/hello"})
	public void hello(Req req, Res res) {
		res.json("msg", "Hello!");
	}
	
	public static void main(String[] args) {
		App.start("sample.kaze");
	}
}
