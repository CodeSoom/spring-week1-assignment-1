package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.exceptions.IllegalHttpRequestException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestMethodException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;
import com.codesoom.assignment.utils.HttpRequestValidator;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final Path path;
    private final String requestBody;

    public HttpRequest(HttpExchange exchange) throws IllegalHttpRequestException {
        this.httpMethod = extractHttpMethod(exchange);
        this.path = extractPath(exchange);
        this.requestBody = extractRequestBody(exchange);

        HttpRequestValidator.checksMissingPartExists(this);
    }

    private String extractRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Path extractPath(HttpExchange exchange) throws IllegalHttpRequestPathException {
        return new Path(exchange);
    }

    private HttpMethod extractHttpMethod(HttpExchange exchange) throws IllegalHttpRequestMethodException {
        String methodName = exchange.getRequestMethod();
        HttpRequestValidator.checksMethodNameValid(methodName);

        return HttpMethod.valueOf(methodName);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Path getPath() {
        return path;
    }

    public String getRequestBody() {
        return requestBody;
    }
}
