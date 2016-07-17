package kaze.http.request;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import kaze.fw.lib.Jackson;

public class Json {

	private HttpServletRequest req;	
	public Json(HttpServletRequest req) {
		this.req = req;
	}

	public <T> Data<T> bind(Class<T> to) {
		return new Data<>(Jackson.toObj(body(), to));
	}

	// TODO check program action, when i close the reader.
	private String body() {
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
