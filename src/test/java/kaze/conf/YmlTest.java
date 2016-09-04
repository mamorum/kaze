package kaze.conf;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class YmlTest {

  @Test
  public void build() {
    
    System.setProperty("kaze.env", "dev");
    System.setProperty("thread.max", "215");
    
    Yml yml = Yml.build();
    
    // test value of _conf.xml
    assertThat(yml.map.get("http.host")).isNull();
    
    // test value of conf.xml (overwrite * 1)
    assertThat(yml.map.get("http.timeout")).isEqualTo(62000);
    
    // test value of conf-dev.xml (overwrite * 2)
    assertThat(yml.map.get("thread.min")).isEqualTo(10);
    
    // test value of jvm arg (overwrite * 3)
    assertThat(yml.map.get("thread.max")).isEqualTo("215");
  }
}
