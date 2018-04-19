package tools;

import kaze.opt.Jetty;

public class JettyThread {
  private static volatile boolean started = false;
  private static final Object lock = new Object();
  private static final Thread thread = new Thread(
    new Runnable() {
      public void run() { Jetty.listen(8080); }
  });

  public static void start() {
    synchronized (lock) {
      if (started) return;
      thread.start();  //<- not main thread
      try { pause(); }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      started = true;
    }
  }
  //-> wait for travis ci.
  private static void pause() throws InterruptedException {
    for (int i = 0; i < 5; i++) {
      Thread.State state = thread.getState();
      if (state == Thread.State.WAITING) break;
      else lock.wait(1000);  // server thread has not joined yet.
    }
  }
}
