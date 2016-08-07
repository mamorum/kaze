package kaze.http;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kaze.http.data.Tool;
import kaze.http.res.Type;

public class Res {
	
	public HttpServletResponse sr;
	public Res(HttpServletResponse sr) { this.sr = sr; }

	private Res write(String body, String defaultType) {
		if (sr.getContentType() == null) {
			sr.setContentType(defaultType);
		}
		try { sr.getWriter().print(body); }
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public Res write(String body) {
	  return write(body, Type.PLAIN);
	}
	
	public Res json(Object src) {
	  if (src instanceof String) return write(
	    (String) src, Type.JSON
	  );
	  return write(
	      Tool.toJson(src), Type.JSON
		);
	}
	
	public Res json(Object... kv) {
	  if (kv.length < 2) return this;
	  if (kv.length == 2) return json(
	    Collections.singletonMap(kv[0], kv[1])
	  );
	  Map<Object, Object> src = new LinkedHashMap<>();
		for (int i = 0; i < kv.length; ) {
			src.put(kv[i], kv[i + 1]);
			i = i + 2;
		}
		return json(src);
	}
	
	public Res type(String type) {
		sr.setContentType(type);
		return this;
	}
	
	public Res status(int i) {
		sr.setStatus(i);
		return this;
	}	
	
	public Res redirect(String url) {
	  sr.setHeader("Location", sr.encodeRedirectURL(url));
	  return this;
	}
}
