package it.tool;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class Env {
  public static final App app = new App();
  static {
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
  }
  private static volatile boolean init = false;
  private static final Object lock = new Object();
  private static final Thread env = new Thread(
    new Runnable() {
      public void run() { Jetty.listen(8080); }
  });

  public static void init() {
    synchronized (lock) {
      if (init) return;
      env.start();  //<- start another thread
      try { pause(); }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      Gson gson = new Gson();
      app.conv(gson::fromJson, gson::toJson);
      init = true;
    }
  }
  //-> wait for travis ci.
  private static void pause() throws InterruptedException {
    for (int i = 0; i < 5; i++) {
      Thread.State state = env.getState();
      if (state == Thread.State.WAITING) break;
      else lock.wait(1000);  // server thread has not joined yet.
    }
  }
}
