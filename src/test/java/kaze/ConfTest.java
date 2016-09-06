package kaze;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConfTest {

  @BeforeClass public static void once() {
    System.setProperty("kaze.env", "dev");
    System.setProperty("thread.max", "215");
  }
  
  @Test public void load() {    
    
    // value of _conf.properties
    assertThat(Conf.get("http.host")).isNull();
    assertThat(Conf.get("static.path")).isEqualTo("/public");
    
    // value of conf.properties (overwrite * 1)
    assertThat(Conf.getInt("http.timeout")).isEqualTo(62000);
    
    // value of conf-dev.properties (overwrite * 2)
    assertThat(Conf.getInt("thread.min")).isEqualTo(10);
    
    // value of jvm arg (overwrite * 3)
    assertThat(Conf.getInt("thread.max")).isEqualTo(215);
    
    // value of app and dev only
    assertThat(Conf.get("app.test")).isEqualTo("app");
    assertThat(Conf.get("dev.test")).isEqualTo("dev");
    
    // no key in all files and jvm arg.
    assertThat(Conf.get("non.test")).isNull();
    assertThat(Conf.getInt("non.test")).isEqualTo(0);
  }
}
