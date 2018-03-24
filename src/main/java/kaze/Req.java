package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest $;
  App app;
  //-> for path param
  String path;
  String[] parts;
  Map<String, Integer> index;
  public Req(
    HttpServletRequest r, String path, String[] parts,
    Map<String, Integer> index, App a
  ) {
    this.$=r; this.path=path; this.parts=parts;
    this.index=index; this.app=a;
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
  public String param(String name) {
    return $.getParameter(name);
  }
  public String path(String name) {
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
}