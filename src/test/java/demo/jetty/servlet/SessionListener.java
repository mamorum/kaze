package demo.jetty.servlet;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
  @Override public void sessionCreated(HttpSessionEvent se) {
    System.out.print(LocalDateTime.now());
    System.out.print(" Session created ");
    System.out.println(se.getSession().getId());
  }
  @Override public void sessionDestroyed(HttpSessionEvent se) {
    System.out.print(LocalDateTime.now());
    System.out.print(" Session destroyed ");
    System.out.println(se.getSession().getId());
  }
}
