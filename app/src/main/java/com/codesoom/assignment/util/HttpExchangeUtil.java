package com.codesoom.assignment.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.stream.Collectors;

public class HttpExchangeUtil {

    private HttpExchangeUtil() {}

    public static String getRequestMethod(final HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    public static String getRequestPath(final HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    public static String extractRequestBody(final HttpExchange exchange) {
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public static void sendHttpResponse(final HttpExchange exchange,
                                        final int statusCode,
                                        final String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
