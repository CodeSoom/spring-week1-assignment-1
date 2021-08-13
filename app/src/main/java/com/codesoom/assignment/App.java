package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
  static final int PORT_NUMBER = 8000;
  static final int BACKLOG_SETTING_NUMBER = 0;

  public static void main(String[] args) {
    System.out.println(new App().getGreeting());

    try {
      InetSocketAddress address = new InetSocketAddress((PORT_NUMBER));
      HttpServer httpServer = HttpServer.create(address, BACKLOG_SETTING_NUMBER);
      HttpHandler handler = new DemoHttpHandler();
      httpServer.createContext("/", handler);
      httpServer.start();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public String getGreeting() {
    return "Hello World!";
  }
}