package kaze.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kaze.http.util.Json;

public class Res {
	
	private boolean isWritten = false;
	private String contentType = null;
	public HttpServletResponse response;
	public Res(HttpServletResponse r) { this.response = r; }

	private Res write(String body, String defaultType) {
		if (this.contentType == null) {
			response.setContentType(defaultType);
		}
		try {
			response.getWriter().print(body);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public Res write(String str) {
		return write(
			str,
			"text/plain;charset=utf-8"
		);
	}
	
	public Res json(Object src) {
		return write(
			Json.of(src),
			"application/json;charset=utf-8"
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
	
	// TODO exception
	public Res contentType(String type) {
		if (isWritten) throw new RuntimeException();
		this.contentType = type;
		return this;
	}
	
	public Res status(int i) {
		response.setStatus(i);
		return this;
	}		
}
