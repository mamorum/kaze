package kaze.ex;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ConvertException extends BadRequestException {

  public ConvertException(Exception e) {
    super(e);
  }

  @Override public Object error() {
    HashMap<String, String> obj = new HashMap<>();
    obj.put("cause", "convert");
    obj.put("msg", getMessage());
    return obj;
  }
}
