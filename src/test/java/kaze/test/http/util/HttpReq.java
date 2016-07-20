package kaze.test.http.util;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class HttpReq {
  
  static HttpRequestFactory requestFactory
    = (new NetHttpTransport()).createRequestFactory();

  public static HttpRes get(String url) throws Exception {
    HttpResponse r = requestFactory.buildGetRequest(
      new GenericUrl(url)
    ).execute();
    return new HttpRes(r);
  }
    
  public static HttpRes postJson(String url, String json) throws Exception {
    HttpResponse r = requestFactory.buildRequest(
        "POST", new GenericUrl(url),
        ByteArrayContent.fromString(
          "application/json", json
        )
    ).execute();
    return new HttpRes(r);
  }
  
  public static HttpRes postParams(String url, String params) throws Exception {
    HttpResponse r = requestFactory.buildRequest(
        "POST", new GenericUrl(url),
        ByteArrayContent.fromString(
          "application/x-www-form-urlencoded", params
        )
    ).execute();
    return new HttpRes(r);
  }
}
