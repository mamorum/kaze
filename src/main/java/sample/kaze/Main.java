package sample.kaze;

import java.util.Collections;

import kaze.App;
import kaze.app.Controller;
import kaze.app.Http;
import kaze.app.Json;
import kaze.app.method.Get;

@Controller
public class Main {
	
	public @Get("/hello") Json hello(Http http) {
		return Json.of(Collections.singletonMap("msg", "Hello!"));
	}
	 
	public static void main(String[] args) {
		App.build("sample.kaze").start();
	}
}
