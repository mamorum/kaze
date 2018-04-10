package tools;

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
  public void statusIs(int s) {
    assertEquals(s, this.status);
  }
  public void typeIsJsonUtf8() {
    assertEquals(
      "application/json;charset=utf-8", this.contentType
    );
  }
  public void typeIs(String type) {
    assertEquals(type, this.contentType);
  }
  public void bodyIs(String b) {
    assertEquals(b, this.body);
  }
  public void bodyContains(String... str) {
    for (String s : str) assertTrue(this.body.contains(s));
  }
  public void bodyIsEmpty() {
    assertTrue(this.body == null || "".equals(body));
  }

  public void printBody() {
    System.out.println(this.body);
  }

  public void close() {
    try { if (res != null) res.disconnect();}
    catch (IOException e) {throw new RuntimeException(e);}
  }
}
