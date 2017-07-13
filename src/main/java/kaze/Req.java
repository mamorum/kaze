package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class Req {
  private static final Gson gson = App.gson;
	public HttpServletRequest srv;
	public Path path;
	public Req(HttpServletRequest r, Path p) {
	  srv = r; path = p;
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
    return path.val(name);
  }
  static class Path {
    private String path;
    private Map<String, Integer> index;
    Path(String p, Map<String, Integer> i) {
      path = p; index = i;
    }
    String val(String name) {
      String[] paths = path.substring(1).split("/");
      return paths[index.get(name)];
    }
  }
}
