package kaze.route;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.Http;

// not thread safe. 
public class Routes {

  private static final Logger logger = LoggerFactory.getLogger(Routes.class);
  
  private HashMap<String, HashMap<String, Handler>>
    method2uri = new HashMap<>();
  
  private HashMap<String, ArrayList<Handler>>
    method2regex = new HashMap<>();
  
  // for fw init.
  public void scan(String... pkgs) {
    Reflections ref = new Reflections(
        pkgs, new MethodAnnotationsScanner()
    );
    for (
      Method m : ref.getMethodsAnnotatedWith(Http.class)
    ) {
      Http http = m.getAnnotation(Http.class);
      String httpMethod = http.value()[0].toUpperCase();
      String httpUri = http.value()[1];
      Func func = Func.of(m);
      add(httpMethod, httpUri, func);
    }
  }
  
  public void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegex(method, uri, func);
    else addUri(method, uri, func);
  }
  
  private void addRegex(String method, String uri, Func func) {
    ArrayList<Handler> regexRoutes = method2regex.get(method);
    if (regexRoutes == null) {
      regexRoutes = new ArrayList<>();
      method2regex.put(method, regexRoutes);
    }
    Handler route = Handler.fromRegexUri(uri, func);
    regexRoutes.add(route);
    log(method, uri, func);
  }

  private void addUri(String method, String uri, Func func) {
    HashMap<String, Handler> uriRoutes = method2uri.get(method);
    if (uriRoutes == null) {
      uriRoutes = new HashMap<>();
      method2uri.put(method, uriRoutes);
    }
    Handler route = Handler.fromUri(uri, func);
    uriRoutes.put(uri, route);
    log(method, uri, func);
  }

  private void log(String method, String uri, Func f) {
    logger.info(
        "[{} {}] -> [{}#{}]", method, uri, 
        f.m.getDeclaringClass().getName(), 
        f.m.getName());
  }

  // for app running.
  public Handler handler(String method, String uri) {    
    // resolve from method2uri
    Handler route = null;
    HashMap<String, Handler> uriRoutes = method2uri.get(method);
    if (uriRoutes != null) route = uriRoutes.get(uri);
    if (route != null) return route;
    
    // resolve from method2regex
    ArrayList<Handler> regexRoutes = method2regex.get(method); 
    if (regexRoutes != null) {
      for(Handler r : regexRoutes) {
        if (r.uriPattern.matcher(uri).matches()) return r;
      }
    }
    
    // not found
    return null;
  }
}
