package all;

public class Case {
  
  private static boolean initialized = false;
  
  static {
    if (!initialized) {
      
      // for testing and using Conf.
      System.setProperty("kaze.env", "dev");
      System.setProperty("thread.max", "215");
      
      initialized = true;
    }
  }
}
