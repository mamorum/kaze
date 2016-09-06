package kaze.conf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class ArgTest {

  @BeforeClass public static void once() {
    System.setProperty("test.arg.d", "d");
    System.setProperty("test.arg.e", "e");
  }
  
  @Test public void d() {
    // property exists.
    String d = Arg.d("test.arg.d");
    assertThat(d).isEqualTo("d");
    
    // does not.
    String e = Arg.d("test.arg.c");
    assertThat(e).isNull();
  }
  
  @Test public void push() {
    Properties p = new Properties();
    p.put("test.arg.d", "dd");
    p.put("test.arg.f", "f");
    Arg.push(p);
    
    // overwrite.
    String d = p.getProperty("test.arg.d");
    assertThat(d).isEqualTo("d");
    
    // not added.
    String e = p.getProperty("test.arg.e");
    assertThat(e).isNull();
    
    // no effect.
    String f = p.getProperty("test.arg.f");
    assertThat(f).isEqualTo("f");
  }
}
