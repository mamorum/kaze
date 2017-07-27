package kaze;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest srv;
  private Path path;

  public Req(HttpServletRequest r, Path path, Route route) {
    this.srv = r; this.path=path;
    this.path.index = route.path.index;
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
    return App.fromJson.exec(body(), to);
  }

  public String param(String name) {
    return srv.getParameter(name);
  }

  public String path(String name) {
    // TODO ":" で始まるかチェック
    Integer i = path.index.get(name);
    if (i == null) throw new RuntimeException("...");
    return path(i);
  }
  public String path(int index) {
    return path.tree[index];
  }
}
