package kaze.fw;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaze.http.Req;
import kaze.http.Res;
import kaze.http.req.Uri;

public class Route {
	
  public Func func;
	public Pattern uriPattern;
	public Map<String, Integer> uriIndex;
	
	public Route(Func func) {
	  this.func = func;
	}
	
	public Route(
	    Func func,
	    Pattern pattern,
	    Map<String, Integer> index
	) {
    this.func = func;
    this.uriPattern = pattern;
    this.uriIndex = index;
  }

  public void run(
      String uri,
      HttpServletRequest req,
      HttpServletResponse res
  ) {
    func.call(
        new Req(req, new Uri(uri, uriIndex)),
        new Res(res)
    );
  }
}
