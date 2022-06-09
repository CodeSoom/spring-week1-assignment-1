package com.codesoom.assignment;

import com.codesoom.assignment.todo.TaskHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

public class SuperHttpHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String sRequestMethod = exchange.getRequestMethod();

    String sRequestPath = requestURI.getPath();

    InputStream inputStream = exchange.getRequestBody();
    String sRequestBody =
        new BufferedReader(new InputStreamReader(inputStream))
            .lines()
            .collect(Collectors.joining("\n"));

    if (sRequestPath.startsWith("/tasks")) {
      TaskHandler taskHandler = new TaskHandler();
      taskHandler.handler(sRequestMethod, sRequestPath, sRequestBody, exchange);
    } else {
      // If response length has the value -1 then no response body is being sent.
      // response 길이가 -1 이라면 response body 가 전송되지 않습니다.
      exchange.sendResponseHeaders(404, -1);
    }
  }
}
