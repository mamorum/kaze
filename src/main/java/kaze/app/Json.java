package kaze.app;

import com.google.gson.Gson;

public class Json {
	
	static Gson gson = new Gson();
	
	Object target;
	
	public static Json of(Object target) {
		Json json = new Json();
		json.target = target;
		return json;
	}

	public String toJson() {
		return gson.toJson(target);
	}
}
