package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest $;
  private String path;
  private String[] paths;
  private Route route;
  private Json.Parse jsnPrs;
  public Req(
    HttpServletRequest s, String path,
    String[] paths, Route rt, Json.Parse jsnPrs
  ) {
    this.$=s; this.path=path; this.paths=paths;
    this.route=rt; this.jsnPrs=jsnPrs;
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
    if (jsnPrs == null) {
      throw new IllegalStateException("No json converter found.");
    }
    return jsnPrs.exec(body(), to);
  }
}