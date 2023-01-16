package com.codesoom.assignment.task.utils;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class HttpClient {

  public static final int EMPTY_RESPONSE_LEN = -1;

  private HttpClient() {
  }

  public static void sendResponse(HttpExchange httpExchange, String content, int statusCode)
      throws IOException {
    if (Objects.isNull(content) || content.isBlank()) {
      httpExchange.sendResponseHeaders(statusCode, EMPTY_RESPONSE_LEN);
      return;
    }
    httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);
    OutputStream outputStream = httpExchange.getResponseBody();
    outputStream.write(content.getBytes());
    outputStream.flush();
    outputStream.close();
  }
}
