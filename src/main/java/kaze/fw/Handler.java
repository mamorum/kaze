package kaze.fw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
	
	void listen();
	
	void handle(
		HttpServletRequest request,
		HttpServletResponse response
	);
}
