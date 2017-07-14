package it;

import static org.junit.Assert.*;

import java.io.IOException;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

public class HttpRes {
  int status;
  String contentType, body;
  HttpResponse res;

  public HttpRes(HttpResponse res) throws IOException {
    this.res = res;
    this.status = res.getStatusCode();
    this.contentType = res.getContentType();
    this.body = res.parseAsString();
  }
  public HttpRes(HttpResponseException e) {
    this.status = e.getStatusCode();
    this.contentType = e.getHeaders().getContentType();
    this.body = e.getContent();
  }

  //-> assertions
  public HttpRes statusIs(int s) {
    assertEquals(s, this.status);
    return this;
  }
  public HttpRes typeIsJsonUtf8() {
    assertEquals(
      "application/json;charset=utf-8", this.contentType
    );
    return this;
  }
  public HttpRes typeIs(String type) {
    assertEquals(type, this.contentType);
    return this;
  }
  public HttpRes bodyIs(String b) {
    assertEquals(b, this.body);
    return this;
  }
  public HttpRes bodyContains(String... str) {
    for (String s : str) assertTrue(this.body.contains(s));
    return this;
  }
  public HttpRes bodyIsEmpty() {
    assertTrue(this.body == null || "".equals(body));
    return this;
  }

  public HttpRes printBody() {
    System.out.println(this.body);
    return this;
  }

  public void close() {
    try { if (res != null) res.disconnect();}
    catch (IOException e) {throw new RuntimeException(e);}
  }
}
