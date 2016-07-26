package kaze.fw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.ex.BadRequestException;

public class Func {

  private static final Logger logger = LoggerFactory.getLogger(Func.class);
  
  public Object o; Method m;
  
  public Func(Object o, Method m) {
    this.o = o; this.m = m;
  }
  
  public void call(Req req, Res res) {
    try {
      m.invoke(o, req, res);
    }
    catch (InvocationTargetException e) {
      Throwable c = e.getCause();
      if (c instanceof BadRequestException) {
        BadRequestException bre =(BadRequestException) c; 
        res.status(bre.status()).json(bre.error());
        logger.debug(c.getMessage(), c);
        return;
      }
      throw new RuntimeException(c);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
