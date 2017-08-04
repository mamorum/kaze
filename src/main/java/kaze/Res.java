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

  // Transfer-Encoding: Chunked
  public void stream(String contentType, String body) {
    srv.setContentType(contentType);
    try {
      srv.getOutputStream().print(body);
      srv.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // content-length
  public void write(String contentType, String body) {
    srv.setContentType(contentType);
    try { srv.getWriter().print(body); }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void html(String html) {
    write("text/html", html);
  }
  public void json(Object obj) {
    write("application/json", App.toJson.exec(obj));
  }
  public void json(Object... kv) {
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
