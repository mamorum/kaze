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
	
	public Res write(String contentType, String body) {
		response.setContentType(contentType);
		try {
			response.getWriter().print(body);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public Res json(Object src) {
		return write(
			"application/json;charset=utf-8",
			gson.toJson(src)
		);
	}
	
	public Res json(Object k, Object v, Object... kv) {
		Map<Object, Object> src = Collections.singletonMap(k, v);
		for (int i = 0; i < kv.length; ) {
			src.put(kv[i], kv[i + 1]);
			i = i + 2;
		}
		return json(src);
	}
	
	public Res status(int i) {
		response.setStatus(i);
		return this;
	}		
}
