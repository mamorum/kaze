package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Req {
  public HttpServletRequest srv;
  String path;
  String[] parts;
  Map<String, Integer> index;
  Conf conf;
  public Req(
    HttpServletRequest r, String path, String[] parts,
    Map<String, Integer> index, Conf conf
  ) {
    this.srv=r; this.path=path; this.parts=parts;
    this.index=index; this.conf=conf;
  }

  public String body() {
    StringBuilder body = new StringBuilder();
    String line;
    try {
      BufferedReader r = srv.getReader();
      while ((line = r.readLine()) != null) {
        body.append(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return body.toString();
  }

  public <T> T json(Class<T> to) {
    return conf.j2o.exec(body(), to);
  }

  public String param(String name) {
    return srv.getParameter(name);
  }

  public String path(String name) {
    // TODO ":" で始まるかチェック
    Integer i = index.get(name);
    if (i == null) throw new RuntimeException("...");
    return parts[i];
  }
}
