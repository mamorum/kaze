package kaze.fw;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// not thread safe. single thread consumes this class.
public class Routes {

  public HashMap<String, HashMap<String, Func>>
    method2uri = new HashMap<>();
  
  public HashMap<String, HashMap<Pattern, Func>>
    method2pattern = new HashMap<>();
  
  // for fw init.
  public void add(String method, String uri, Func func) {
    if (uri.contains(":")) addRegexUri(method, uri, func);
    else addUri(method, uri, func);
  }
  
  // Create URI index and, 
  // Change URI to Pattern (ex. "/:id/:name/" to "/.*/.*")
  private void addRegexUri(String method, String uri, Func func) {
    
    Map<String, Integer> index = new HashMap<>();
    StringBuilder regex = new StringBuilder();
    String[] path = uri.substring(1).split("/");
    for (int i = 0; i < path.length; i++) {
      regex.append("/");
      if (path[i].contains(":")) {
        regex.append("[^/]+");
        index.put(path[i], i);
      } else {
        regex.append(path[i]);
      }
    }
    func.uriIndex = index;
    
    addPattern(
        method, Pattern.compile(regex.toString()), func
    );
  }
  
  private void addPattern(String method, Pattern uri, Func func) {
    HashMap<Pattern, Func> uri2func = method2pattern.get(method);
    if (uri2func == null) {
      uri2func = new HashMap<>();
      method2pattern.put(method, uri2func);
    }
    uri2func.put(uri, func);    
  }

  private void addUri(String method, String uri, Func func) {
    HashMap<String, Func> uri2func = method2uri.get(method);
    if (uri2func == null) {
      uri2func = new HashMap<>();
      method2uri.put(method, uri2func);
    }
    uri2func.put(uri, func);    
  }

  // for app running.
  public Func get(String method, String uri) {
    
    Func func = null;
    
    // resolve from method2uri
    HashMap<String, Func> uri2func = method2uri.get(method);
    if (uri2func != null) func = uri2func.get(uri);
    if (func != null) return func;
    
    // resolve from method2pattern
    HashMap<Pattern, Func> pattern2func
                  = method2pattern.get(method); 
    if (pattern2func != null) {
      for(Map.Entry<Pattern, Func> e : pattern2func.entrySet()) {
        if (e.getKey().matcher(uri).matches()) return e.getValue();
      }
    }
    
    // not found
    return null;
  }
}
