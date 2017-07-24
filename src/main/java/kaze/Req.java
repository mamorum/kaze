package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class Req {
  static final Gson gson = new Gson();
  public HttpServletRequest srv;
  public String method;
  public String path;
  public String[] paths;
  public Map<String, String> pathParam;

  public Req(HttpServletRequest r, String p) {
    srv = r; method=r.getMethod(); path = p;
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
    return pathParam.get(name);
  }
}
