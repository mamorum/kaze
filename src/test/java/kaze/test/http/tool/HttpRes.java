package kaze.test.http.tool;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

public class HttpRes {

  int status;
  String contentType, body;
  
  public HttpRes(HttpResponse res) throws IOException { 
    this.status = res.getStatusCode();
    this.contentType = res.getContentType();
    this.body = res.parseAsString();
  }
  
  public HttpRes(HttpResponseException e) {
    this.status = e.getStatusCode();
    this.contentType = e.getHeaders().getContentType();
    this.body = e.getContent();
  }
  
  public HttpRes statusIs(int status) {
    assertThat(this.status).isEqualTo(status);
    return this;
  }
  
  public HttpRes contentTypeIsJson() {
    assertThat(this.contentType).isEqualTo(
        "application/json;charset=utf-8"
    );
    return this;
  }
  
  public HttpRes bodyIs(String body) {
    assertThat(this.body).isEqualTo(body);
    return this;
  }
  
  public HttpRes bodyContains(String... str) {
    for (String s : str) assertThat(this.body).contains(s);
    return this;
  }

  public HttpRes printBody() {
    System.out.println(this.body);
    return this;
  }
}
