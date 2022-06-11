package com.codesoom.assignment.todo.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class Response {
  private final int responseStatusCode;
  private final String responseContent;

  public static final int OK = 200;
  public static final int CREATED = 201;
  public static final int NO_CONTENT = 204;
  public static final int BAD_REQUEST = 400;
  public static final int NOT_FOUND = 404;
  public static final int CONFLICT = 409;

  public Response(int responseStatusCode, String responseContent) {
    this.responseStatusCode = responseStatusCode;
    this.responseContent = responseContent;
  }

  public void sendResponse(HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(
        responseStatusCode, Objects.requireNonNull(responseContent).getBytes().length);
    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(responseContent.getBytes());
    outputStream.flush();
    outputStream.close();
  }
}
