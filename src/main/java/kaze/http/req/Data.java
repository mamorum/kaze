package kaze.http.req;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import kaze.http.Tool;

public class Data<T> {

  private T obj;
  private Data(T obj) { this.obj = obj; }

  public T get() { return obj; }
  public T valid() { Tool.validate(obj); return obj; }
  
  public static <T> Data<T> jsonToObj(
    HttpServletRequest sr, Class<T> to
  ) {
    return new Data<>(
        Tool.toObj(json(sr), to)
    );
  }

  private static String json(HttpServletRequest sr) {
    try {
      // TODO set encoding when get params.
      if (sr.getCharacterEncoding() == null) {
        sr.setCharacterEncoding("utf-8");
      }     
      BufferedReader r = sr.getReader();
      StringBuilder body = new StringBuilder();
      String line = null;
      while ((line = r.readLine()) != null) {
        body.append(line);
      }
      return body.toString();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static <T> Data<T> paramsToObj(
      HttpServletRequest sr, Class<T> to
  ) {
    try {
      T o = to.newInstance();
      for (Field f : to.getDeclaredFields()) {
        setParam(sr, o, f, f.getName());
      }
      return new Data<>(o);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void setParam(
      HttpServletRequest sr,
      Object o, Field f, String name
  ) {
    // resolve
    Class<?> fType = f.getType();
    Object val = null;  
    if (
      fType.isArray() ||
      Collection.class.isAssignableFrom(fType)
    ) {
      val = sr.getParameterValues(name);
    }
    else {
      val = sr.getParameter(name);
    }
    
    // no value in request
    if (val == null) return;
    
    // set
    try {
      f.setAccessible(true);
      f.set(o, Tool.convert(val, fType));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
