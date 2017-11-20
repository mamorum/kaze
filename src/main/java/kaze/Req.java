package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kaze.App.Json2obj;

public class Req {
  public HttpServletRequest srv;
  private String[] pathTree;
  private Map<String, Integer> pathIndex;
  private Json2obj json2obj;

  public Req(HttpServletRequest r,
    String[] ptree, Path path, Json2obj j2o) {
    this.srv = r;
    this.pathTree=ptree;
    this.pathIndex=path.index;
    this.json2obj=j2o;
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
    return json2obj.exec(body(), to);
  }

  public String param(String name) {
    return srv.getParameter(name);
  }

  public String path(String name) {
    // TODO ":" で始まるかチェック
    Integer i = pathIndex.get(name);
    if (i == null) throw new RuntimeException("...");
    return path(i);
  }
  public String path(int index) {
    return pathTree[index];
  }
}
