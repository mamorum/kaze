package kaze.http;

import javax.servlet.http.HttpServletRequest;

import kaze.http.request.Json;
import kaze.http.request.Param;
import kaze.http.request.Uri;

public class Req {

	public HttpServletRequest servletReq;
	public Param param;
	public Json json;
	public Uri uri;
	
	public Req(HttpServletRequest sr) {
		this.servletReq = sr;
		this.json = new Json(sr);
		this.param = new Param(sr);
	}
}
