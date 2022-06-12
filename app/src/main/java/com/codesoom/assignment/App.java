package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
  private static final String HOST = "localhost";
  private static final int PORT = 8000;

  public static void main(String[] args) {

    InetSocketAddress address = new InetSocketAddress(HOST, PORT);

    try {
      HttpServer httpServer = HttpServer.create(address, 0);
      HttpHandler superHttpHandler = new SuperHttpHandler();

      httpServer.createContext("/", superHttpHandler);
      httpServer.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
