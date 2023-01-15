package com.codesoom.assignment.task.utils;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class HttpClient {

  private HttpClient() {
  }

  public static void sendResponse(HttpExchange httpExchange, String content, int statusCode)
      throws IOException {
    httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);
    OutputStream outputStream = httpExchange.getResponseBody();
    outputStream.write(content.getBytes());
    outputStream.flush();
    outputStream.close();
  }
}
