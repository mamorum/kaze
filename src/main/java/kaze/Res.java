package kaze;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class Res {
  public HttpServletResponse $;
  App app;
  public Res(HttpServletResponse r, App a) {
    this.$=r; this.app=a;
  }

  public Res status(int status) {
    $.setStatus(status);
    return this;
  }

  // Transfer-Encoding: Chunked
  public void stream(String contentType, String body) {
    $.setContentType(contentType);
    try {
      $.getOutputStream().print(body);
      $.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // content-length
  public void write(String contentType, String body) {
    $.setContentType(contentType);
    try { $.getWriter().print(body); }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void html(String html) {
    write("text/html", html);
  }
  public void json(String json) {
    write("application/json", json);
  }
  public void json(Object obj) {
    json(app.o2j.exec(obj));
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
    $.setStatus(status);
    $.setHeader("Location", $.encodeRedirectURL(url));
  }
}
