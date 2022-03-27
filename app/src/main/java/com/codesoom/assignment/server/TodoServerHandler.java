package com.codesoom.assignment.server;

import com.codesoom.assignment.common.logger.Log;
import com.codesoom.assignment.common.request.HttpMethod;
import com.codesoom.assignment.common.request.HttpRequest;
import com.codesoom.assignment.common.response.HttpResponse;
import com.codesoom.assignment.common.util.TodoUtils;
import com.codesoom.assignment.config.AppConfig;
import com.codesoom.assignment.domain.mapping.HttpMapping;
import com.codesoom.assignment.domain.todo.TodoService;
import com.codesoom.assignment.infrastructure.mapping.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TodoServerHandler implements HttpHandler {
    public final AppConfig appConfig;
    private final Logger logger = Log.getInstance().getLog(TodoServerHandler.class);


    public TodoServerHandler(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String requestBody = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody())
        ).lines().collect(Collectors.joining("\n"));


        Map<String, Function<TodoService, HttpMapping>> httpMethodToConstructorMap = getHttpMethodToConstructorMap();
        HttpRequest httpRequest = toHttpRequest(path, method, requestBody);
        HttpMapping httpMapping = httpMethodToConstructorMap.get(method).apply(appConfig.todoService());
        HttpResponse httpResponse = httpMapping.process(httpRequest);
        writeHttpResponse(exchange, httpResponse);

    }

    private HttpRequest toHttpRequest(String path, String method, String requestBody) {
        return new HttpRequest.HttpRequestBuilder()
                .path(path)
                .method(method)
                .requestBody(requestBody)
                .builder();
    }

    private Map<String, Function<TodoService, HttpMapping>> getHttpMethodToConstructorMap() {
        Map<String, Function<TodoService, HttpMapping>> httpMethodToConstructorMap = new HashMap<>();
        httpMethodToConstructorMap.put(HttpMethod.GET.getMethod(), GetHttpMapping::new);
        httpMethodToConstructorMap.put(HttpMethod.POST.getMethod(), PostHttpMapping::new);
        httpMethodToConstructorMap.put(HttpMethod.PUT.getMethod(), PutHttpMapping::new);
        httpMethodToConstructorMap.put(HttpMethod.DELETE.getMethod(), DeleteHttpMapping::new);
        httpMethodToConstructorMap.put(HttpMethod.PATCH.getMethod(), PatchHttpMapping::new);
        return httpMethodToConstructorMap;
    }

    private void writeHttpResponse(HttpExchange httpExchange, HttpResponse httpResponse) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            byte[] body = TodoUtils.convertToBytes(httpResponse);
            httpExchange.sendResponseHeaders(httpResponse.getStatusCode(), body.length);
            outputStream.write(body);
            outputStream.flush();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

}
