package kaze.fw;

public class Arg {
  
  public static Dev dev = new Dev();
  
  // return val from arg "-Dkey=val".
  public static String d(String key) {
    return System.getProperty(key);
  }

  // TODO overwrite config with system properties.
  public static Conf push(Conf conf) {
    String httpPort = d("httpPort");
    if (httpPort == null) return conf;
    conf.server.httpPort = Integer.parseInt(httpPort);
    
    String staticDir = d("staticDir");
    if (staticDir == null) return conf;
    conf.server.staticDir = staticDir;
    return conf;
    
    // TODO logging in class Conf.
//    log.info(
//      "Change http port to system property {} {}",
//      "[key=" + key + "]", "[val=" + p + "]"
//    );
  }
  
  public static class Dev {
    public String syncUrl() { return d("syncUrl"); }
  }
}
