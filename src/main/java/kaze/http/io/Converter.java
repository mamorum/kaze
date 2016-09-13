package kaze.http.io;

import kaze.ex.ConvertException;
import kaze.lib.Jackson;

public class Converter {

  public static <T> T convert(Object val, Class<T> to) {
    try { return Jackson.convert(val, to); }
    catch (IllegalArgumentException e) {
      throw new ConvertException(e);
    }
  }  

}
