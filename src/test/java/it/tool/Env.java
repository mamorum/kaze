package it.tool;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class Env {
  private static volatile boolean init = false;
  private static final Object lock = new Object();
  private static final Thread env = new Thread(
    new Runnable() {
      @Override public void run() {
        Gson gson = new Gson();
        App.parser(gson::fromJson, gson::toJson);
        Jetty.doc("/public", "/");
        Jetty.listen(8080);
      }
  });

  public static void init() {
    synchronized (lock) {
      if (init) return;
      env.start();
      try { pause(); }  //<- main thread
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      init = true;
    }
  }

  // for travis ci.
  private static void pause() throws InterruptedException {
    for (int i = 0; i < 5; i++) {
      Thread.State state = env.getState();
      if (state == Thread.State.WAITING) break;
      else lock.wait(1000);  // server thread has not joined yet.
    }
  }
}
