package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest $;
  App app;
  String[] paths;
  Route route; //-> for path param
  public Req(
    HttpServletRequest r, App a, String[] parts, Route p
  ) {
    this.$=r; this.app=a; this.paths=parts; this.route=p;
  }

  public String param(String name) {
    return $.getParameter(name);
  }
  public String path(String name) {
    if (route.index == null) {
      throw new IllegalStateException(
        "Path parameter not defined. " +
        "[path=" + route.path + "]"
      );
    }
    Integer i = route.index.get(name);
    if (i == null) {
      throw new IllegalArgumentException(
        "Path parameter not found [arg=" + name + "]. " +
        "Arg must be started with ':' " +
        "(like \":id\", \":name\", etc)."
      );
    }
    return paths[i];
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