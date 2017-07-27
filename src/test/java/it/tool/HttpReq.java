package it.tool;

import java.io.IOException;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;

public class HttpReq {
  static HttpRequestFactory requestFactory
    = (new NetHttpTransport()).createRequestFactory();

  public static HttpRes get(String url) {
    try {
      HttpResponse r = requestFactory.buildGetRequest(
          new GenericUrl(url)
      ).execute();
      return new HttpRes(r);
    }
    catch (HttpResponseException e) {
      return new HttpRes(e);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public static HttpRes postJson(String url, String json) {
    try {
      HttpResponse r = requestFactory.buildRequest(
          "POST", new GenericUrl(url),
          ByteArrayContent.fromString(
            "application/json", json
          )
      ).execute();
      return new HttpRes(r);
    }
    catch (HttpResponseException e) {
      return new HttpRes(e);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public static HttpRes postForm(String url, String params) {
    try {
      HttpResponse r = requestFactory.buildRequest(
          "POST", new GenericUrl(url),
          ByteArrayContent.fromString(
            "application/x-www-form-urlencoded", params
          )
      ).execute();
      return new HttpRes(r);
    }
    catch (HttpResponseException e) {
      return new HttpRes(e);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
