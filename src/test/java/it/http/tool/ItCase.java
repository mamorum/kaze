package it.http.tool;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import all.Case;
import kaze.App;

public class ItCase extends Case {
  
  protected static final Logger log = LoggerFactory.getLogger(ItCase.class);
  
  static volatile boolean serving = false;
  static final Object lock = new Object();
  static final Thread t = new Thread(
    new Runnable() {
      @Override public void run() {
        App.start(
            "it.http.main",
            "it.http.req",
            "it.http.res"
        );
      }
  }); 
  
  @BeforeClass
  public static void serve() throws InterruptedException {
    synchronized (lock) {
      if (serving) return;
      t.start();
      pause();
      serving = true;
    }
  }

  // for trabvis ci.
  private static void pause() throws InterruptedException {
    int time = 0;  
    String v = System.getProperty("testWait");
    if (v != null) time = Integer.parseInt(v);
    log.info("Wait time is {}ms for server starting", time);   
    if (time != 0) lock.wait(time);
  }
}
