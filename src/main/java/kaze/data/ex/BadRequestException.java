package kaze.data.ex;

import java.util.HashMap;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {
  
  public BadRequestException(Throwable t) { super(t); }
  public BadRequestException(String msg) { super(msg); }

  public int status() { return 400; }
  
  public Object error() {
    HashMap<String, String> obj = new HashMap<>();
    obj.put("cause", "request");
    obj.put("msg", getMessage());
    return obj;
  } 
}
