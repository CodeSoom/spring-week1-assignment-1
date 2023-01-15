package com.codesoom.assignment.task.utils;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Parser {

  private Parser() {
  }

  public static String parseRequestBody(HttpExchange httpExchange) {
    InputStream inputStream = httpExchange.getRequestBody();
    return new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining());
  }

  public static Long extractId(String path, String basePath) {
    return Long.parseLong(path.substring(basePath.length() + 1));
  }
}
