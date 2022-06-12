package com.codesoom.assignment.todo.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

public class Request {

  private String requestMethod;
  private String requestPath;
  private String requestBody;
  private String requestQuery;

  public Request(HttpExchange exchange) {
    URI requestURI = exchange.getRequestURI();
    this.requestMethod = exchange.getRequestMethod();
    this.requestPath = requestURI.getPath();
    InputStream inputStream = exchange.getRequestBody();
    this.requestBody =
        new BufferedReader(new InputStreamReader(inputStream))
            .lines()
            .collect(Collectors.joining("\n"));
    this.requestQuery = requestURI.getQuery();
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public String getRequestPath() {
    return requestPath;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public String getRequestQuery() {
    return requestQuery;
  }
}
