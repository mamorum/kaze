package kaze.http.ex;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;

import kaze.fw.ex.Recoverable;
import kaze.http.Req;
import kaze.http.Res;

@SuppressWarnings("serial")
public class ValidateException
  extends RuntimeException implements Recoverable {
  
  private Set<ConstraintViolation<Object>> scv;
    
  public ValidateException(Set<ConstraintViolation<Object>> scv) {
    super("Validation Error");
    this.scv = scv;
  }
  
  @Override
  public void respond(Req req, Res res) {
    response.apply(req, res, this);
  }

  // If other response is needed, change it before kaze.App#start.
  public static Recoverable.Response<Req, Res, ValidateException>
    response = (req, res, err) ->
  {
    ArrayList<ErrorProperty> ep =  new ArrayList<>();
    for (ConstraintViolation<Object> cv : err.scv) {
      ep.add(new ErrorProperty(
          cv.getPropertyPath().toString(),
          cv.getConstraintDescriptor().getAnnotation()
                  .annotationType().getSimpleName(),
          cv.getMessage()
      ));
    }    
    res.status(400).json(
        "cause", "validate",
        "fields", ep
    );
  };
  
  static class ErrorProperty {
    String name, cause, msg;
    ErrorProperty(String n, String c, String m) {
      name = n; cause = c; msg = m; 
    }
  }
}
