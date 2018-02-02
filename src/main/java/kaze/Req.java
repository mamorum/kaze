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
  Json json;
  public Req(
    HttpServletRequest r, String path, String[] parts,
    Map<String, Integer> index, Json json
  ) {
    this.srv=r; this.path=path; this.parts=parts;
    this.index=index; this.json=json;
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
    return json.toObj.exec(body(), to);
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
