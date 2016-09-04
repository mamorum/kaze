package kaze;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.conf.Arg;
import kaze.conf.Property;

public class Conf {

  private static final Logger log = LoggerFactory.getLogger(Conf.class);
  
  private static Properties kv;
  public static Server server;
  
  static {
    kv = Property.load();
    Arg.push(kv);
    server = new Server();
  }
  
  public static String get(String key) {
    String v = kv.getProperty(key);
    if (v == null) return null;
    if (v.length() == 0) return null;
    return v;
  }
  
  public static int getInt(String key) {
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
    public Server() {
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
