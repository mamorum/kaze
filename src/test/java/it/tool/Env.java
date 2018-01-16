package it.tool;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class Env {
  public static final App app = new App();
  public static final Gson gson = new Gson();
  public static void initJsonParser() {
    app.conf.parser(gson::fromJson, gson::toJson);
  }
  static {
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    initJsonParser();
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
