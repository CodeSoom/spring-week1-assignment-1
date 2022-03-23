package com.codesoom.assignment.handlers;

import com.codesoom.assignment.controllers.TodoController;
import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.utils.JsonParser;
import com.codesoom.assignment.utils.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private final Logger logger;
    private final TodoController todoController;

    public TodoHttpHandler() {
        this.logger = new Logger();
        this.todoController = new TodoController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String reqMethod = httpExchange.getRequestMethod();
        URI reqUri = httpExchange.getRequestURI();
        String reqPath = reqUri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String reqBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        logger.logRequest(reqMethod, reqUri, reqPath, reqBody);


        String resBody = proceed(reqMethod, reqPath, reqBody);


        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resBody.getBytes(StandardCharsets.UTF_8).length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(resBody.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        logger.logResponse(resBody);
    }

    private String proceed(String reqMethod, String reqPath, String reqBody) throws IOException {
        if (reqMethod.equals(HttpMethod.GET.getMethod()) && reqPath.equals("/tasks")) {
            return todoController.getTodos();
        }

        if (reqMethod.equals(HttpMethod.GET.getMethod()) && reqPath.equals("/tasks/{id}")) {
            //TODO
            return null;
        }

        if (reqMethod.equals(HttpMethod.POST.getMethod()) && reqPath.equals("/tasks")) {
            return todoController.postTodo(JsonParser.toTask(reqBody));
        }

        if (reqMethod.equals(HttpMethod.PUT.getMethod()) && reqPath.equals("/tasks/{id}")) {
            //TODO
            return null;
        }

        if (reqMethod.equals(HttpMethod.DELETE.getMethod()) && reqPath.equals("/tasks/{id}")) {
            //TODO
            return null;
        }

        return "";
    }

}
