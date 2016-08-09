package kaze.http.ex;

import java.util.function.BiConsumer;

import kaze.http.Res;

@SuppressWarnings("serial")
public class ConvertException
  extends RuntimeException implements Recoverable {
  
  public ConvertException(Exception e) {
    super(e);
  }

  @Override public void reply(Res res) {
    response.accept(res, this);
  }
  
  // If other response is needed, change it before kaze.App#start.
  public static BiConsumer<Res, ConvertException>
    response = (res, e) ->
  {
    res.status(400).json(
        "cause", "convert",
        "msg", e.getMessage()
    );
  };
}
