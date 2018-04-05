package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest $;
  App app;
  String[] parts;
  Path addedPath; //-> for path param
  public Req(
    HttpServletRequest r, App a, String[] parts, Path p
  ) {
    this.$=r; this.app=a; this.parts=parts; this.addedPath=p;
  }

  public String param(String name) {
    return $.getParameter(name);
  }
  public String path(String name) {
    if (addedPath.index == null) {
      throw new IllegalStateException(
        "Path parameter not defined. " +
        "[path=" + addedPath.path + "]"
      );
    }
    Integer i = addedPath.index.get(name);
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