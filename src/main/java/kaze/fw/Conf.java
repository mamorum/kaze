package kaze.fw;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import kaze.App;

public class Conf {

  public Http http;
  Conf() { http = new Http(); }

  public static class Http {
    public int 
      maxThread=200, minThread=8, threadTimeout=60000,
      port=8080, timeout=30000;
    public String
      host, staticDir, staticPath="/public";
    @Override
    public String toString() {
      StringBuilder s = new StringBuilder();
      return s.append("[host=").append(host)
          .append(", port=").append(port)
          .append(", timeout=").append(timeout)
          .append("] [thread: max=").append(maxThread)
          .append(", min=").append(minThread)
          .append(", timeout=").append(threadTimeout)
          .append("] [static: dir=").append(staticDir)
          .append(", path=").append(staticPath)
          .append("]").toString();
    }
  }

  public static Conf build() {
    InputStream s = App.class.getResourceAsStream("/conf.yml");
    if (s == null) return new Conf();  // original setting.
    return (new Yaml()).loadAs(s, Conf.class);// default setting.
  }
}
