package kaze.route;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import kaze.http.Req;
import kaze.http.Res;

public class Func {

  private static Map<String, Object> cache = new HashMap<>();
  
  Method m; Object o;
  private Func(Method m, Object o) {
    this.m = m; this.o = o;
  }

  public static Func of(Method m) {
    Class<?> c = m.getDeclaringClass();
    String n = c.getName();
    Object o = cache.get(n);
    if (o == null) o = create(n, c); 
    return new Func(m, o);
  }
  
  public static Object create(String n, Class<?> c) {
    try {
      Object o = c.newInstance();
      cache.put(n, o);
      return o;
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
