package kaze.http.ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.validation.ConstraintViolation;

@SuppressWarnings("serial")
public class ValidateException extends BadRequestException {
  
  Set<ConstraintViolation<Object>> scv;
    
  public ValidateException(Set<ConstraintViolation<Object>> scv) {
    super("Validation Error.");
    this.scv = scv;
  }
  
  @Override public Object error() {
    ArrayList<ErrorProperty> ep =  new ArrayList<>();
    for (ConstraintViolation<Object> cv : scv) {
      ep.add(new ErrorProperty(
          cv.getPropertyPath().toString(),
          cv.getConstraintDescriptor().getAnnotation()
                  .annotationType().getSimpleName(),
          cv.getMessage()
      ));
    }    
    HashMap<String, Object> err = new HashMap<>();
    err.put("cause", "validate");
    err.put("fields", ep);
    
    return err;
  }
  
  public static class ErrorProperty {
    public String name, cause, msg;
    public ErrorProperty(String n, String c, String m) {
      name = n; cause = c; msg = m; 
    }
  }
}
