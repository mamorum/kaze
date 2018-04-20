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
  public void write(String contentType, String toSend) {
    $.setContentType(contentType);
    try { $.getWriter().print(toSend); }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void json(String toSend) {
    write("application/json", toSend);
  }
  public void json(Object toSend) {
    if (jsnStrfy == null) {
      throw new IllegalStateException("No json converter found.");
    }
    json(jsnStrfy.exec(toSend));
  }
  public void json(Object... kvToSend) {
    if (kvToSend.length == 2) {
      json(Collections.singletonMap(
        kvToSend[0], kvToSend[1])
      );
      return;
    }
    int size = kvToSend.length / 2;
    Map<Object, Object> src = new LinkedHashMap<>(size);
    for (int i=0; i<kvToSend.length; i=i+2) {
      src.put(kvToSend[i], kvToSend[i+1]);
    }
    json(src);
  }
  public void html(String toSend) {
    write("text/html", toSend);
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
