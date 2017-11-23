package it.tool;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

public class Env {
  public static final App app = new App();
  public static final Gson gson = new Gson();
  private static final Jetty jetty = new Jetty();
  static {
    jetty.app(app, "/app/*");
    jetty.doc("/public", "/");
  }
  private static volatile boolean init = false;
  private static final Object lock = new Object();
  private static final Thread env = new Thread(
    new Runnable() {
      public void run() { jetty.listen(8080); }
  });

  public static void init() {
    synchronized (lock) {
      if (init) return;
      env.start();  //<- start another thread
      try {
        pause(); //<- main thread
        checkNoJsonParser();
        app.parser(gson::fromJson, gson::toJson);
      } catch (InterruptedException e) {
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
  private static void checkNoJsonParser() {
    app.get("/nojson/req", (req, res) -> {
      try { req.json(String.class); }
      catch (IllegalStateException e) {
        System.out.println(e);
      }
      res.status(501);
    });
    app.get("/nojson/res", (req, res) -> {
      try { res.json("msg", "nojson"); }
      catch (IllegalStateException e) {
        System.out.println(e);
      }
      res.status(501);
    });
    HttpRes res;
    res = HttpReq.get(
      "http://localhost:8080/app/nojson/req"
    );
    res.statusIs(501).close();
    res = HttpReq.get(
      "http://localhost:8080/app/nojson/res"
    );
    res.statusIs(501).close();
  }
}
