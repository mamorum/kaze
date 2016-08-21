package kaze.fw.lib;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import kaze.fw.Conf;

// for single thread, initializing application conf.
public class Snake {
  
  private static Yaml yml = new Yaml();
  
  public static Conf load(InputStream is) {
    return yml.loadAs(is, Conf.class);
  }
}
