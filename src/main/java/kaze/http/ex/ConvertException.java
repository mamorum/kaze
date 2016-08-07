package kaze.http.ex;

import kaze.fw.ex.Recoverable;
import kaze.http.Req;
import kaze.http.Res;

@SuppressWarnings("serial")
public class ConvertException
  extends RuntimeException implements Recoverable {
  
  public ConvertException(Exception e) {
    super(e);
  }

  @Override
  public void respond(Req req, Res res) {
    response.apply(req, res, this);
  }
  
  // If other response is needed, change it before kaze.App#start.
  public static Recoverable.Response<Req, Res, ConvertException>
    response = (req, res, err) ->
  {
    res.status(400).json(
        "cause", "convert",
        "msg", err.getMessage()
    );
  };
}
