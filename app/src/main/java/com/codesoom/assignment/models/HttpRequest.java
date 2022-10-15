package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.exceptions.IllegalHttpRequestBodyException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestMethodException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;
import com.codesoom.assignment.utils.HttpRequestValidator;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {

    private final HttpExchange exchange;
    private HttpMethod httpMethod;
    private Path path;
    private String requestBody;

    public HttpRequest(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public HttpMethod getHttpMethod() throws IllegalHttpRequestMethodException {
        if (httpMethod != null) {
            return httpMethod;
        }

        final String methodName = exchange.getRequestMethod();
        HttpRequestValidator.checksMethodNameValid(methodName);

        httpMethod = HttpMethod.valueOf(methodName);
        return httpMethod;
    }

    public Path getPath() throws IllegalHttpRequestPathException, IllegalHttpRequestMethodException {
        if (path != null) {
            return path;
        }

        path = new Path(exchange);
        HttpRequestValidator.checksIdMissed(getHttpMethod(), path.getId());
        return path;
    }

    public String getRequestBody() throws IllegalHttpRequestBodyException, IllegalHttpRequestMethodException {
        if (requestBody != null) {
            return requestBody;
        }

        final InputStream inputStream = exchange.getRequestBody();
        requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        HttpRequestValidator.checksRequestBodyMissed(getHttpMethod(), requestBody);
        return requestBody;
    }
}
