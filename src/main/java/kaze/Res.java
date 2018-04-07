package kaze;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kaze.App.Obj2json;

public class Res {
  public HttpServletResponse $;
  private Obj2json o2j;
  Res(HttpServletResponse s, Obj2json o) {
    this.$=s; this.o2j=o;
  }

  public Res status(int status) {
    $.setStatus(status);
    return this;
  }
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
    if (o2j == null) {
      throw new IllegalStateException("No json parser found.");
    }
    json(o2j.exec(obj));
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
  // 公開するか検討中 ->
  void redirect(int status, String url) {
    $.setStatus(status);
    $.setHeader("Location", $.encodeRedirectURL(url));
  }
  void stream(String contentType, String body) {
    $.setContentType(contentType);
    try {
      //-> Jetty だと Transfer-Encoding: Chunked になる
      $.getOutputStream().print(body);
      $.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
