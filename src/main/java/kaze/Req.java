package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kaze.App.Json2obj;

public class Req {
  public HttpServletRequest srv;
  Json2obj json2obj;
  String[] pathTree;
  Map<String, Integer> pathIndex;

  public Req(HttpServletRequest r, Json2obj j2o) {
    this.srv = r; this.json2obj=j2o;
  }
  public static Req from(HttpServletRequest r, App ap)
    throws UnsupportedEncodingException {
    if (r.getCharacterEncoding() == null) {
      r.setCharacterEncoding(ap.encoding);
    }
    return new Req(r, ap.j2o);
  }

  // TODO delete
  public Req(HttpServletRequest r, Json2obj j2o,
    String[] pathTree, Map<String, Integer> pathIndex
  ) {
    this.srv = r;
    this.json2obj=j2o;
    this.pathTree=pathTree;
    this.pathIndex=pathIndex;
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
