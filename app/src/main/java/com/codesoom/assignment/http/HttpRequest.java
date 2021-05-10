package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 클라이언트의 HTTP 요청을 나타냅니다.
 */
public class HttpRequest {
    public static String readBody(HttpExchange exchange) {
        var inputStream = exchange.getRequestBody();
        var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new BufferedReader(inputStreamReader)
            .lines()
            .collect(Collectors.joining(System.lineSeparator()));
    }
}
