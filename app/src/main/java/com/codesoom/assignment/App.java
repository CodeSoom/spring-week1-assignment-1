package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

  public static void main(String[] args) {

    System.out.println(new App().getGreeting());

    try {
      int portNumber = 8000;
      int backlogSettingNumber = 0;

      InetSocketAddress address = new InetSocketAddress((portNumber));
      HttpServer httpServer = HttpServer.create(address, backlogSettingNumber);
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