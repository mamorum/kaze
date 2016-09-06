package kaze;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.conf.Arg;
import kaze.conf.Pfile;

public class Conf {

  private static final Logger log = LoggerFactory.getLogger(Conf.class);
  
  private static Properties kv;
  public static Svr svr;
  
  static {
    kv = Pfile.load();
    Arg.push(kv);
    svr = new Svr();
  }
  
  public static String get(String key) {
    String v = kv.getProperty(key);
    if (v == null) return null;
    if (v.length() == 0) return null;
    return v;
  }
  
  // return 0, if value is null or empty 
  public static int getInt(String key) {
    String v = get(key);
    if (v == null) return 0;
    return Integer.valueOf(v);
  }
  
  public static class Svr {
    public int
      threadMin, threadMax,
      threadTimeout, httpPort, httpTimeout;
    public String
      httpHost, staticDir, staticPath;
    public Svr() {
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
