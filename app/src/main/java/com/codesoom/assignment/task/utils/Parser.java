package com.codesoom.assignment.task.utils;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Parser {

  private final String method;
  private final String path;
  private final String body;

  public Parser(HttpExchange httpExchange) {
    this.method = httpExchange.getRequestMethod();
    this.path = httpExchange.getRequestURI().getPath();
    this.body = parseBody(httpExchange);
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public String getBody() {
    return body;
  }

  private String parseBody(HttpExchange httpExchange) {
    InputStream inputStream = httpExchange.getRequestBody();
    return new BufferedReader(new InputStreamReader(inputStream)).lines()
        .collect(Collectors.joining());
  }

  public long getId(String basePath) {
    return Long.parseLong(path.substring(basePath.length() + 1));
  }
}
