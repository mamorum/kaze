package kaze.server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

// Jetty WebSocket
public class Jws {
  private static List<Class<?>> endpoints = new ArrayList<>();
  private static Consumer<ServletContextHandler> ws = (sch) -> {
    try {
      ServerContainer ws =
        WebSocketServerContainerInitializer.configureContext(sch);
      for (Class<?> ep: endpoints) ws.addEndpoint(ep);
    } catch (DeploymentException | ServletException e) {
      throw new RuntimeException(e);
    }
  };
  public static void add(Class<?> svrEndpoint) {
    endpoints.add(svrEndpoint);
  }
  public static void install() {
    Jetty.ws = ws;
  }
}