package kaze.test.http.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.api.client.http.HttpResponse;

public class HttpRes {

  HttpResponse res;
  public HttpRes(HttpResponse res) { this.res = res; }
  
  public HttpRes statusIs(int status) {
    assertThat(res.getStatusCode()).isEqualTo(status);
    return this;
  }
  
  public HttpRes contentTypeIsJson() {
    assertThat(
        res.getContentType()
    ).isEqualTo("application/json;charset=utf-8");
    return this;
  }
  
  public HttpRes bodyIs(String body) throws Exception{
    assertThat(res.parseAsString()).isEqualTo(body);
    return this;
  }
}
