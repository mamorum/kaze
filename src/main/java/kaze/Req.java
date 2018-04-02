package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest $;
  App app;
  //-> for path param
  Map<String, Integer> index;
  String[] parts;
  public Req(
    HttpServletRequest r, App a, Map<String, Integer> index, String[] parts
  ) {
    this.$=r; this.app=a; this.index=index; this.parts=parts;
  }

  public String param(String name) {
    return $.getParameter(name);
  }
  public String path(String name) {
    // TODO index の null チェック？
    Integer i = index.get(name);
    if (i == null) {
      throw new IllegalArgumentException(
        "Path parameter not found [arg=" + name + "]. " +
        "Arg must be started with ':' " +
        "(like \":id\", \":name\", etc)."
      );
    }
    return parts[i];
  }
  public String body() {
    StringBuilder body = new StringBuilder();
    String line;
    try {
      BufferedReader r = $.getReader();
      while ((line = r.readLine()) != null) {
        body.append(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return body.toString();
  }
  public <T> T json(Class<T> to) {
    if (app.j2o == null) {
      throw new IllegalStateException("No json parser found.");
    }
    return app.j2o.exec(body(), to);
  }
}