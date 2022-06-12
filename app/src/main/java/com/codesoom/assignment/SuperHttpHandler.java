package com.codesoom.assignment;

import com.codesoom.assignment.todo.TaskHandler;
import com.codesoom.assignment.todo.models.Request;
import com.codesoom.assignment.todo.models.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SuperHttpHandler implements HttpHandler {

  private static final String TASK_PATH = "/tasks";
  private static final int EMPTY_RESPONSE_BODY_LENGTH = -1;

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Request request = new Request(exchange);
    String requestPath = request.getRequestPath();

    if (requestPath.startsWith(TASK_PATH)) {
      TaskHandler taskHandler = new TaskHandler();
      Response response = taskHandler.handler(request);
      response.sendResponse(exchange);
    } else {
      // If response length has the value -1 then no response body is being sent.
      // response 길이가 -1 이라면 response body 가 전송되지 않습니다.
      exchange.sendResponseHeaders(Response.NOT_FOUND, EMPTY_RESPONSE_BODY_LENGTH);
    }
  }
}
