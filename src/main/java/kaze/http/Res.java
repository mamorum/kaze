package kaze.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class Res {
	
	static final Gson gson = new Gson();
	public HttpServletResponse response;
	public Res(HttpServletResponse r) { this.response = r; }
	
	private void writeBody(String s) {
		try {
			response.getWriter().print(s);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}
	
	public Res jsonFrom(Object src) {
		response.setContentType("application/json;charset=utf-8");
		writeBody(gson.toJson(src));
		return this;
	}
	
	public Res jsonFom(Object k, Object v, Object... kv) {
		Map<Object, Object> map = Collections.singletonMap(k, v);
		for (int i = 0; i < kv.length; ) {
			map.put(kv[i], kv[i + 1]);
			i = i + 2;
		}
		return jsonFrom(map);
	}

	public Res status(int i) {
		response.setStatus(i);
		return this;
	}		
}
