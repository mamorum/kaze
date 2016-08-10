package kaze.it.http.tool;

import org.junit.BeforeClass;

import kaze.App;

public class ItCase {
  
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
  
  @BeforeClass
  public static void beforeHttpTest() throws InterruptedException {
    synchronized (lock) {
      if (serving) return;
      t.start();
      lock.wait(2500);
      serving = true;
    }
  }
}
