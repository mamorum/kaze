package kaze;

import java.io.File;

public class App {
  //-> http
  private String host=null;
  private int httpTime=30000;
  //-> thread
  private int max=200, min=8, threadTime=60000;
  //-> static files
  private String path=null;
  private File dir=null;

  //-> server settings
  public void http(String host, int timeout) {
    this.host = host;
    this.httpTime = timeout;
  }
  public void thread(int max, int min, int timeout) {
    this.max = max;
    this.min = min;
    this.threadTime = timeout;
  }
  public void location(String classpath) {
    this.path = classpath;
  }
  public void location(File dir) {
    this.dir = dir;
  }

  //-> routing (Methods in "org.eclipse.jetty.http.HttpMethod")
  public void get(String path, Func f) { Routes.add("GET", path, f); }
  public void post(String path, Func f) { Routes.add("POST", path, f); }
  public void head(String path, Func f) { Routes.add("HEAD", path, f); }
  public void put(String path, Func f) { Routes.add("PUT", path, f); }
  public void options(String path, Func f) { Routes.add("OPTIONS", path, f); }
  public void delete(String path, Func f) { Routes.add("DELETE", path, f); }
  public void trace(String path, Func f) { Routes.add("TRACE", path, f); }
  public void connect(String path, Func f) { Routes.add("CONNECT", path, f); }
  public void move(String path, Func f) { Routes.add("MOVE", path, f); }
  public void proxy(String path, Func f) { Routes.add("PROXY", path, f); }
  public void pri(String path, Func f) { Routes.add("PRI", path, f); }

  //-> start
  public void listen(int port) {
    Lib.startJetty(
      host, port, httpTime, max, min, threadTime, path, dir
    );
  }
}
