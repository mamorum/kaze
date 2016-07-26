package kaze.http.req;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import kaze.http.lib.Converter;

public class Json {

  private HttpServletRequest sreq;
  private Json(HttpServletRequest sreq) { this.sreq = sreq; }
  public static Json of(HttpServletRequest sreq) {
    return new Json(sreq);
  }
  
	public <T> T to(Class<T> to) {
	  return Converter.toObj(json(), to);
	}

	// TODO check program action, when i close the reader.
	private String json() {
		try {			
			if (sreq.getCharacterEncoding() == null) {
				sreq.setCharacterEncoding("utf-8");
			}			
			BufferedReader r = sreq.getReader();
			StringBuilder body = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				body.append(line);
			}
			return body.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
