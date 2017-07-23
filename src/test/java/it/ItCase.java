package it;

import org.junit.BeforeClass;

import kaze.App;
import kaze.server.Jetty;

public class ItCase {
  protected static App app = new App();
  protected static Jetty jetty = new Jetty(app);
  private static volatile boolean serving = false;
  private static final Object lock = new Object();
  private static final Thread t = new Thread(
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
    for (int i = 0; i < 5; i++) {
      Thread.State state = t.getState();
      System.out.println((new StringBuilder()
        ).append("Server state is "
        ).append(state.toString().toLowerCase()
      ));
      if (state == Thread.State.WAITING) break;
      else lock.wait(1000);  // server has not joined yet.
    }
  }
}
