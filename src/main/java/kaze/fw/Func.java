package kaze.fw;

import java.lang.reflect.Method;

import kaze.http.Req;
import kaze.http.Res;

public class Func {
  
  public Object o; Method m;
  
  public Func(Object o, Method m) {
    this.o = o; this.m = m;
  }
  
  public void call(Req req, Res res) {
    try { m.invoke(o, req, res); }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
