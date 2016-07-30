package kaze.it.http.tool;

import org.junit.BeforeClass;

import kaze.it.http.AppStartMain;

public class ItCase {
  
  static final Object lock = new Object();
  static volatile Thread t;
  
  @BeforeClass
  public static void beforeHttpTest() throws InterruptedException {
    synchronized (lock) {
      if (t != null) return;
      t = new Thread(new Runnable() {
        @Override public void run() {
          AppStartMain.main(null);
        }
      });
      t.start();
      lock.wait(5000);
    }
  }
}
