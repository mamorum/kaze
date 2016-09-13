package kaze.http.io;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import kaze.ex.ConvertException;
import kaze.lib.Jackson;

public class Json {

  public static String str(Object from) {
    if (from instanceof String) return (String) from;
    try {
      return Jackson.om.writeValueAsString(from);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }

  public static <T> T obj(String json, Class<T> to) {
    try {
      return Jackson.om.readValue(json, to);
    } catch (Exception e) {
      throw new ConvertException(e);
    }
  }

  public static <T> T bind(
    HttpServletRequest from, Class<T> to
  ) {
    try {
      BufferedReader reqbody = from.getReader();
      StringBuilder json = new StringBuilder();
      String line = null;
      while ((line = reqbody.readLine()) != null) {
        json.append(line);
      }
      return obj(json.toString(), to);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
