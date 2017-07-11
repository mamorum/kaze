package kaze;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class Res {
	public HttpServletResponse srv;
	public Res(HttpServletResponse r) { this.srv = r; }

  public void send(String body, String type) throws IOException{
    srv.setContentType(type);
    srv.getWriter().print(body);
  }

	public void json(Object src) throws IOException {
	  send(Lib.gsn.toJson(src), "application/json");
	}
	public void json(Object... kv) throws IOException {
	  if (kv.length == 2) {
	    json(Collections.singletonMap(kv[0], kv[1]));
	    return;
	  }
	  int size = kv.length / 2;
	  Map<Object, Object> src = new LinkedHashMap<>(size);
		for (int i=0; i<kv.length; i=i+2) {
			src.put(kv[i], kv[i+1]);
		}
		json(src);
	}

  public void redirect(int status, String url) {
    srv.setStatus(status);
    srv.setHeader("Location", srv.encodeRedirectURL(url));
  }

	public Res status(int status) {
		srv.setStatus(status);
		return this;
	}
  public Res type(String type) {
    srv.setContentType(type);
    return this;
  }
}
