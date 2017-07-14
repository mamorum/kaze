package it;

import org.junit.BeforeClass;

import kaze.App;
import kaze.server.Jetty;

public class ItCase {
  protected static App app = new App();
  protected static Jetty jetty = new Jetty(app);
  static volatile boolean serving = false;
  static final Object lock = new Object();
  static final Thread t = new Thread(
    new Runnable() {
      @Override public void run() {
        jetty.listen(8080);
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

  // for travis ci.
  private static void pause() throws InterruptedException {
    lock.wait(5000);
//    for (int i = 1; i < 6; i++) {
//      if (Jetty.started) break;
//      lock.wait(1000);
//    }
  }
}
