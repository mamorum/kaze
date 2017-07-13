package kaze;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class Res {
	public HttpServletResponse srv;
	public Res(HttpServletResponse r) { this.srv = r; }

  public Res status(int status) {
    srv.setStatus(status);
    return this;
  }

  public void send(String contentType, String body) throws IOException {
    srv.setContentType(contentType);
    srv.getWriter().print(body);
  }
  public void send(String body) throws IOException{
    send("text/plain", body);
  }
  public void html(String html) throws IOException{
    send("text/html", html);
  }
  public void json(Object obj) throws IOException {
    send("application/json", App.gson.toJson(obj));
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
}
