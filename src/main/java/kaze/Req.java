package kaze;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Req {
	public HttpServletRequest srv;
	public Path path;
	public Req(HttpServletRequest r, Path p) {
	  srv = r; path = p;
	}

  public <T> T json(Class<T> to) throws IOException {
    BufferedReader body = srv.getReader();
    StringBuilder json = new StringBuilder();
    String line = null;
    while ((line = body.readLine()) != null) {
      json.append(line);
    }
    return Lib.gsn.fromJson(json.toString(), to);
  }

  public String param(String name) {
    return srv.getParameter(name);
  }

  public String path(String name) {
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
