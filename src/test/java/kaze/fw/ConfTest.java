package kaze.fw;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ConfTest {

  @Test
  public void build() {
    
    System.setProperty("kaze.env", "dev");
    System.setProperty("thread.max", "215");
    
    Conf conf = Conf.build();
    
    // test value of _conf.xml
    assertThat(conf.server.httpHost).isNull();
    
    // test value of conf.xml (overwrite * 1)
    assertThat(conf.server.httpTimeout).isEqualTo(62000);
    
    // test value of conf-dev.xml (overwrite * 2)
    assertThat(conf.server.threadMin).isEqualTo(10);
    
    // test value of jvm arg (overwrite * 3)
    assertThat(conf.server.threadMax).isEqualTo(215);
  }
}
