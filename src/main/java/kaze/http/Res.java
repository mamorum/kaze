package kaze.http;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kaze.http.tool.Converter;

public class Res {
	
	private boolean isWritten = false;
	public HttpServletResponse sr;
	public Res(HttpServletResponse sr) { this.sr = sr; }

	private Res write(String body, String defaultType) {
		if (sr.getContentType() == null) {
			sr.setContentType(defaultType);
		}
		try {
			sr.getWriter().print(body);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public Res write(String body) {
		return write(
			body,
			"text/plain;charset=utf-8"
		);
	}
	
	public Res json(Object src) {
		return write(
		  Converter.toJson(src),
			"application/json;charset=utf-8"
		);
	}
	
	public Res json(Object... kv) {
	  if (kv.length == 2) {
	    return json(
	        Collections.singletonMap(kv[0], kv[1])
	    );
	  }	
	  Map<Object, Object> src = new LinkedHashMap<>();
		for (int i = 0; i < kv.length; ) {
			src.put(kv[i], kv[i + 1]);
			i = i + 2;
		}
		return json(src);
	}
	
	// TODO exception
	public Res contentType(String type) {
		if (isWritten) throw new RuntimeException();
		sr.setContentType(type);
		return this;
	}
	
	public Res status(int i) {
		sr.setStatus(i);
		return this;
	}		
}
