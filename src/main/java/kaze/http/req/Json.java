package kaze.http.req;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import kaze.http.lib.Jackson;

public class Json {

	public static <T> Data<T> convert(
	    HttpServletRequest req, Class<T> to
	) {
	  String json = body(req);
	  try {
	    return new Data<>(
	        Jackson.om.readValue(json, to)
	    );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
	}

	// TODO check program action, when i close the reader.
	private static String body(HttpServletRequest req) {
		try {			
			if (req.getCharacterEncoding() == null) {
				req.setCharacterEncoding("utf-8");
			}			
			BufferedReader r = req.getReader();
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
