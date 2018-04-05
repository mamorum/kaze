package kaze;

import javax.servlet.http.HttpServletRequest;

class Path {
  private static String root = "";
  private static String[] roots = {root};
  private static String sla = "/";
  //-> common
  static String[] split(String path) {
    if (root.equals(path)) return roots;
    String trimed = path.substring(1); // "/1/2" -> "1/2"
    return trimed.split(sla); // "1/2" -> {"1", "2"}
  }
  //-> for init (add route)
  static String get(String from) {
    if (sla.equals(from)) return root;
    return from;
  }
  //-> for runtime (find route)
  static String get(HttpServletRequest from) {
    String c = from.getContextPath(); //-> /ctxt
    String s = from.getServletPath(); //-> /srvlt
    String r = from.getRequestURI(); //-> /ctxt/srvlt/1/2
    String path = r.substring( //-> /1/2
      c.length() + s.length()
    );
    if (sla.equals(path)) return root;
    return path;
  }
}
