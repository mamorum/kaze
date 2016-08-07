package kaze.fw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kaze.http.Req;
import kaze.http.Res;

public class Func {

  Method m; Object o;
  private Func(Method m, Object o) {
    this.m = m; this.o = o;
  }

  public static Func of(Method m) {
     try {
       return new Func(
         m, m.getDeclaringClass()
                 .newInstance()
         );
    } catch (
      InstantiationException |
      IllegalAccessException e
    ) {
      throw new RuntimeException(e);
    }
   }
  
  void call(Req req, Res res) throws Throwable {
    try { m.invoke(o, req, res);}
    catch (InvocationTargetException e) {
      // InvocationTargetException wraps a cause.
      throw e.getCause();
    }
  }
}
