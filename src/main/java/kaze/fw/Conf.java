package kaze.fw;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.fw.lib.Snake;

public class Conf {

  private static final Logger log = LoggerFactory.getLogger(Conf.class);
  
  public Server server;  

  private static Conf original() {
    Conf c = new Conf();
    c.server = new Server();
    return c;
  }

  public static Conf build() {
    Conf conf = null;
    
    InputStream is = Conf.class.getResourceAsStream("/conf.yml");
    if (is == null) conf = original();
    else conf = Snake.load(is);
    
    String env = Arg.d("kaze.env");
    if (env == null) return Arg.push(conf);
    
    String eyml = "/conf-" + env + ".yml";
    InputStream eis = Conf.class.getResourceAsStream(eyml);
    if (eis == null) return Arg.push(conf);
    
    // TODO overwrite conf, using env specific yaml.
    // Conf econf = Snake.load(eis);
    // ... 
    return Arg.push(conf);
  }

  public static class Server {
    public int 
      maxThread=200, minThread=8, threadTimeout=60000,
      httpPort=8080, httpTimeout=30000;
    public String
      httpHost, staticDir, staticPath="/public";
    public void log() {
      log.info(msg,
          httpHost, httpPort, httpTimeout,
          minThread, maxThread, threadTimeout,
          staticDir, staticPath
      );
    }
    private static final String msg = "Server " + 
        "[http: host={}, port={}, timeout={}] " + 
        "[thread: min={}, max={}, timeout={}] " +
        "[static: dir={}, path={}]";
  }
}
