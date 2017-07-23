package kaze;

public class App {
  public Routes routes = new Routes();

  //-> routing (Methods in "org.eclipse.jetty.http.HttpMethod")
  public void get(String path, Func f) { routes.add("GET", path, f); }
  public void post(String path, Func f) { routes.add("POST", path, f); }
  public void head(String path, Func f) { routes.add("HEAD", path, f); }
  public void put(String path, Func f) { routes.add("PUT", path, f); }
  public void options(String path, Func f) { routes.add("OPTIONS", path, f); }
  public void delete(String path, Func f) { routes.add("DELETE", path, f); }
  public void trace(String path, Func f) { routes.add("TRACE", path, f); }
  public void connect(String path, Func f) { routes.add("CONNECT", path, f); }
  public void move(String path, Func f) { routes.add("MOVE", path, f); }
  public void proxy(String path, Func f) { routes.add("PROXY", path, f); }
  public void pri(String path, Func f) { routes.add("PRI", path, f); }

  @FunctionalInterface public interface Func {
    void accept(Req req, Res res) throws Throwable;
  }
}
