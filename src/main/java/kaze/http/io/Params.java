package kaze.http.io;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

public class Params {
  
  public static <T> T bind(
      HttpServletRequest from, Class<T> to
  ) {
    try {
      T o = to.newInstance();
      for (Field f : to.getDeclaredFields()) {
        setParam(from, o, f, f.getName());
      }
      return o;
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
      f.set(o, Converter.convert(val, fType));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
