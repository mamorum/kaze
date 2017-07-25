package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class Req {
  static final Gson gson = new Gson();
  public HttpServletRequest srv;
  private String[] paths;
  private Route route;

  public Req(HttpServletRequest r, String[] paths, Route route) {
    this.srv = r; this.paths=paths; this.route=route;
  }

  public String body() throws IOException {
    BufferedReader r = srv.getReader();
    StringBuilder body = new StringBuilder();
    String line;
    while ((line = r.readLine()) != null) {
      body.append(line);
    }
    return body.toString();
  }

  public <T> T json(Class<T> to) throws IOException {
    return gson.fromJson(body(), to);
  }

  public String param(String name) {
    return srv.getParameter(name);
  }

  public String path(String name) {
    // TODO ":" で始まるかチェック
    Integer i = route.params.get(name);
    if (i == null) throw new RuntimeException("...");
    return paths[i];
  }
}
