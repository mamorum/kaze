package kaze;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.conf.Arg;
import kaze.fw.Routes;
import kaze.fw.lib.Jetty;

public class App {
  
  public static Routes routes;

  public static void start(String... pkgs) {
    Sw.start();
    build(pkgs);
    Jetty.start();
    Sw.stop();
    browserSync();
    Jetty.listen();
  }

  public static void build(String... pkgs) {
    routes = Routes.build(pkgs);
  }
  
  private static void browserSync() {
    String url = Arg.d("syncUrl");
    if (url == null) return;
    if (!url.startsWith("http")) return;
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) (
          new URL(url)
      ).openConnection();
      int status = con.getResponseCode();
      log.debug(syncMsg, status, url);
    } catch (IOException e) {
      log.debug(syncMsg, "fail", url, e);
    } finally {
      if (con != null) con.disconnect();
    }
  }
  
  private static final String syncMsg
    = "Requested to BrowserSync [status={}] [url={}]";

  private static final Logger log
    = LoggerFactory.getLogger(App.class);
  
  // Stopwatch
  private static class Sw {
    private static long start;
    static void start() {
      start = System.currentTimeMillis();
    }
    static void stop() {
      long started = System.currentTimeMillis();
      log.info(
          "Started in {}ms ( jvm uptime {}ms )",
          started - start,
          ManagementFactory
            .getRuntimeMXBean().getUptime()
      );
    }
  }  
}