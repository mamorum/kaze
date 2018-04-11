package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import kaze.App.Json2obj;

public class Req {
  public HttpServletRequest $;
  private Json2obj j2o;
  private Route route;
  Req(HttpServletRequest s, Json2obj j, Route r) {
    this.$=s; this.j2o=j; this.route=r;
  }

  public String param(String name) {
    return $.getParameter(name);
  }
  public String path(String name) {
    if (route.index == null) {
      throw new IllegalStateException(
        "Path parameter not defined " +
        "[path=" + route.path + "]."
      );
    }
    Integer i = route.index.get(name);
    if (i == null) {
      throw new IllegalArgumentException(
        "Path parameter not found " +
        "[arg=" + name + "] [path=" + route.path + "]. " +
        "Arg must be started with ':' (ex. \":id\", \":name\")."
      );
    }
    return route.paths[i];
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