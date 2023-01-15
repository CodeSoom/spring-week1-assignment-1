package com.codesoom.assignment;

import com.codesoom.assignment.task.TaskFactory;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {

  public static final int PORT = 8000;
  public static final String BASE_PATH = "/";

  public static void main(String[] args) {
    try {
      HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
      httpServer.createContext(BASE_PATH, TaskFactory.taskHandler());
      httpServer.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
