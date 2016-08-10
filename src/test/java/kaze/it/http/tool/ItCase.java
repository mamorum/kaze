package kaze.it.http.tool;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.App;

public class ItCase {
  
  protected static final Logger log = LoggerFactory.getLogger(ItCase.class);
  
  static volatile boolean serving = false;
  static final Object lock = new Object();
  static final Thread t = new Thread(
    new Runnable() {
      @Override public void run() {
        App.start(
            "kaze.it.http.main",
            "kaze.it.http.req",
            "kaze.it.http.res"
        );
      }
  });
  static int time = 0;
  static {
    String v = System.getProperty("testWait");
    if (v != null) time = Integer.parseInt(v);
    log.info("Wait time is {}ms for server starting", time);
  }
  
  @BeforeClass
  public static void beforeHttpTest() throws InterruptedException {
    synchronized (lock) {
      if (serving) return;
      t.start();
      serving = true;
      if (time != 0) lock.wait(time);
    }
  }
}
