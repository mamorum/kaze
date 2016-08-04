package kaze;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Http {
  String[] value();
  
  // See "org.eclipse.jetty.http.HttpMethod"
  public class Method {
    public static final String
      GET = "GET",
      POST = "POST",
      HEAD = "HEAD",
      PUT = "PUT",
      OPTIONS = "OPTIONS",
      DELETE = "DELETE",
      TRACE = "TRACE",
      CONNECT = "CONNECT",
      MOVE = "MOVE",
      PROXY = "PROXY",
      PRI = "PRI";
  }
}
