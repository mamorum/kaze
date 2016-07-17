package kaze.fw;

import java.util.HashMap;

// not thread safe. single thread consumes this class.
public class Routes {

  public HashMap<String, HashMap<String, Func>>
    method2map = new HashMap<>();

  public void add(String method, String uri, Func fnc) {
    HashMap<String, Func> uri2api = method2map.get(method);
    if (uri2api == null) {
      uri2api = new HashMap<>();
      method2map.put(method, uri2api);
    }
    uri2api.put(uri, fnc);
  }
  
  public Func get(String method, String uri) {
    return method2map.get(method).get(uri);
  }
   
}
