package kaze;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class Res {
  public HttpServletResponse $;
  private Json.Stringify jsnStrfy;
  public Res(HttpServletResponse s, Json.Stringify jsnStrfy) {
    this.$=s; this.jsnStrfy=jsnStrfy;
  }

  public void status(int status) {
    $.setStatus(status);
  }
  public void write(String contentType, String body) {
    $.setContentType(contentType);
    try { $.getWriter().print(body); }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void json(String json) {
    write("application/json", json);
  }
  public void json(Object obj) {
    if (jsnStrfy == null) {
      throw new IllegalStateException("No json converter found.");
    }
    json(jsnStrfy.exec(obj));
  }
  public void json(Object... kv) {
    if (kv.length == 2) {
      json(Collections.singletonMap(
        kv[0], kv[1])
      );
      return;
    }
    int size = kv.length / 2;
    Map<Object, Object> src = new LinkedHashMap<>(size);
    for (int i=0; i<kv.length; i=i+2) {
      src.put(kv[i], kv[i+1]);
    }
    json(src);
  }
  public void html(String html) {
    write("text/html", html);
  }
  //-> TODO Decide to make methods public.
  void redirect(int status, String url) {
    $.setStatus(status);
    $.setHeader("Location", $.encodeRedirectURL(url));
  }
  void stream(String contentType, String body) {
    $.setContentType(contentType);
    try {
      // Jetty makes response as "Transfer-Encoding: Chunked",
      // when we use "HttpServletResponse.getOutputStream().print(String)".
      $.getOutputStream().print(body);
      $.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
