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
      checkNoJsonParser();
      Gson gson = new Gson();
      app.json.parser(gson::fromJson, gson::toJson);
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
  //-> for json parser
  public static void checkNoJsonParser() {
    //-> add function
    app.get.add("/nojson/req", (req, res) -> {
      try { req.json(String.class); }
      catch (IllegalStateException e) {
        System.out.println("Req#json: No Parser ->");
        System.out.println(e);
        res.status(501);
      }
    });
    app.get.add("/nojson/res", (req, res) -> {
      try { res.json("msg", "nojson"); }
      catch (IllegalStateException e) {
        System.out.println("Res#json: No Parser ->");
        System.out.println(e);
        res.status(501);
      }
    });
    //-> check
    ////-> request (from json)
    HttpRes res = null;
    int status = 0;
    res = HttpReq.get(
      "http://localhost:8080/app/nojson/req"
    );
    status = res.status;
    res.close();
    if (status == 200) {
      throw new IllegalStateException("Json Parser found.");
    }
    ////-> response (to json)
    res = HttpReq.get(
      "http://localhost:8080/app/nojson/res"
    );
    status = res.status;
    res.close();
    if (status == 200) {
      throw new IllegalStateException("Json Parser found.");
    }
  }
}
