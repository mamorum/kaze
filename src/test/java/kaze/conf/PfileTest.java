package kaze.conf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class PfileTest {

  @BeforeClass public static void once() {
    System.setProperty("kaze.env", "dev");
  }
  
  @Test public void load() {    
    Properties kv = Pfile.load();
    Arg.push(kv);
    
    // value of _conf.properties
    assertThat(kv.get("http.host")).isEqualTo("");
    assertThat(kv.get("static.path")).isEqualTo("/public");
    
    // value of conf.properties (overwrite * 1)
    assertThat(kv.get("http.timeout")).isEqualTo("62000");
    
    // value of conf-dev.properties (overwrite * 2)
    assertThat(kv.get("thread.min")).isEqualTo("10");
    
    // value of jvm arg (overwrite * 3)
//    assertThat(kv.get("thread.max")).isEqualTo("215");
    
    // value of app and dev only
    assertThat(kv.get("app.test")).isEqualTo("app");
    assertThat(kv.get("dev.test")).isEqualTo("dev");
    
    // no key in all files.
    assertThat(kv.get("non.test")).isNull();
  }
}
