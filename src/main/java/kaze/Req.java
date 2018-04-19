package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import kaze.json.J2o;

public class Req {
  public HttpServletRequest $;
  private String path;
  private String[] paths;
  private Route route;
  private J2o j2o;
  Req(
    HttpServletRequest s, String[] paths,
    Route r, J2o j
  ) {
    this.$=s; this.paths=paths;
    this.route=r; this.j2o=j;
  }

  public String param(String name) {
    return $.getParameter(name);
  }
  public String path(String name) {
    if (route.index == null) {
      throw new IllegalStateException(
        "Path parameter not defined " +
        "[route=" + route.path + "]."
      );
    }
    Integer i = route.index.get(name);
    if (i == null) {
      throw new IllegalArgumentException(
        "Path parameter not found [path=" + path + "] " +
        "[route=" + route.path + "] [arg=" + name + "]. " +
        "Arg must be started with ':' (ex. \":id\", \":name\")."
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
    if (j2o == null) {
      throw new IllegalStateException("No json parser found.");
    }
    return j2o.exec(body(), to);
  }
}