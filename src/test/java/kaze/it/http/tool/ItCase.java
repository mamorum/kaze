package kaze.it.http.tool;

import org.junit.BeforeClass;

import kaze.App;

public class ItCase {
  
  static final Object lock = new Object();
  static volatile Thread t;
  
  @BeforeClass
  public static void beforeHttpTest() throws InterruptedException {
    synchronized (lock) {
      if (t != null) return;
      t = new Thread(new Runnable() {
        @Override public void run() {
          App.start("kaze.it.http.req", "kaze.it.http.res");
        }
      });
      t.start();
      lock.wait(5000);
    }
  }
}
