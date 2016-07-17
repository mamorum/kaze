package kaze.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation represents Http Method (like GET, POST, etc) 
 * and URI ("/hello"). Use like bellow ...</p>
 * 
 * <pre>
 * @Route({"GET", "/hello"})
 * public void hello(Req req, Res res) {
 *   ...
 * }
 * </pre>
 * 
 * <p>The Example code means that
 * Http Request "GET /hello" is handled by
 * <code>#hello(Req req, Res res)</code>.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
  
  /**
   * <p>value()[0] is Http Method name.
   * value()[1] is URI.</p>
   * 
   * @return Array {"Method", "URI"}.
   */
  String[] value();
}
