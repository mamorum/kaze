package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kaze.Conf.Json2obj;

public class Req {
  public HttpServletRequest srv;
  public String uri;
  String[] pathTree;
  Map<String, Integer> pathIndex;
  Json2obj json2obj;

  public Req(
    HttpServletRequest r, String uri, String[] tree,
    Map<String, Integer> index, Json2obj j2o
  ) {
    this.srv=r; this.uri=uri; this.pathTree=tree;
    this.pathIndex=index; this.json2obj=j2o;
  }
  public static Req from(
    HttpServletRequest r, String uri, String[] tree,
    Map<String, Integer> index, Conf c
  )
    throws UnsupportedEncodingException {
    if (r.getCharacterEncoding() == null) {
      r.setCharacterEncoding(c.encoding);
    }
    return new Req(r, uri, tree, index, c.j2o);
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
