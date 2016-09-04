package kaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.conf.Yml;

public class Conf {

  private static final Logger log = LoggerFactory.getLogger(Conf.class);
  
  private static Yml yml;
  public static Server server;
  
  static {
    yml = Yml.build();
    server = new Server(yml);
  }
  
  public static String get(String key) {
    return (String) yml.map.get(key);
  }
  
  public static int getInt(String key) {
    Object val = yml.map.get(key);
    if (val instanceof Integer) {
      return (Integer) val;
    }
    return Integer.valueOf(
      get(key)
    );
  }
  
  public static class Server {
    public int
      threadMin, threadMax,
      threadTimeout, httpPort, httpTimeout;
    public String
      httpHost, staticDir, staticPath;
    public Server(Yml y) {
      httpHost = get("http.host");
      httpPort = getInt("http.port");
      httpTimeout = getInt("http.timeout");
      threadMin = getInt("thread.min");
      threadMax = getInt("thread.max");
      threadTimeout = getInt("thread.timeout");
      staticDir = get("static.dir");
      staticPath = get("static.path");
    }
    public void log() {
      log.info(msg,
        httpHost, httpPort, httpTimeout,
        threadMin, threadMax, threadTimeout,
        staticDir, staticPath);
    }
    private static final String msg = "Server " + 
      "[http: host={}, port={}, timeout={}] " + 
      "[thread: min={}, max={}, timeout={}] " +
      "[static: dir={}, path={}]";
  }
}
