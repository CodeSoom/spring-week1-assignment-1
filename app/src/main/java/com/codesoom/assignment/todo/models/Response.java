package com.codesoom.assignment.todo.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class Response {
  private final int iResponseStatusCode;
  private final String sResponseContent;

  public static final int OK = 200;
  public static final int CREATED = 201;
  public static final int NO_CONTENT = 204;
  public static final int BAD_REQUEST = 400;
  public static final int NOT_FOUND = 404;
  public static final int CONFLICT = 409;

  public Response(int iResponseStatusCode, String sResponseContent) {
    this.iResponseStatusCode = iResponseStatusCode;
    this.sResponseContent = sResponseContent;
  }

  public void sendResponse(HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(
        iResponseStatusCode, Objects.requireNonNull(sResponseContent).getBytes().length);
    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(sResponseContent.getBytes());
    outputStream.flush();
    outputStream.close();
  }
}
