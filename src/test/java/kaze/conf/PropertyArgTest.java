package kaze.conf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.Test;

public class PropertyArgTest {

  @Test
  public void test() {
    
    System.setProperty("kaze.env", "dev");
    System.setProperty("thread.max", "215");
    
    Properties kv = Property.load();
    Arg.push(kv);
    
    // test value of _conf.properties
    assertThat(kv.get("http.host")).isEqualTo("");
    
    // test value of conf.properties (overwrite * 1)
    assertThat(kv.get("http.timeout")).isEqualTo("62000");
    
    // test value of conf-dev.properties (overwrite * 2)
    assertThat(kv.get("thread.min")).isEqualTo("10");
    
    // test value of jvm arg (overwrite * 3)
    assertThat(kv.get("thread.max")).isEqualTo("215");
  }
}
