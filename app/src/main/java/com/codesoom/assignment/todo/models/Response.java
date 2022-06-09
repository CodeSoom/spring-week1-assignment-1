package com.codesoom.assignment.todo.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class Response {
  private final int iResponseStatusCode;
  private final String sResponseContent;

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
