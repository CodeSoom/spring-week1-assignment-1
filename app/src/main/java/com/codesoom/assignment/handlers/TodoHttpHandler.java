package com.codesoom.assignment.handlers;

import com.codesoom.assignment.controllers.TodoController;
import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.enums.HttpStatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.networks.BaseResponse;
import com.codesoom.assignment.utils.JsonParser;
import com.codesoom.assignment.utils.Logger;
import com.codesoom.assignment.utils.PathChecker;
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
        String reqBodyString = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task reqBody = null;
        if (!reqBodyString.isEmpty()) {
            reqBody = JsonParser.toTask(reqBodyString);
        }

        logger.logRequest(reqMethod, reqUri, reqPath, reqBody);


        BaseResponse resBody = proceed(reqMethod, reqPath, reqBody);

        int httpStatusCode = resBody.getStatusCode();
        String resContent = resBody.getStatusMessage();
        Object body = resBody.getBody();

        if (body != null) {
            resContent = JsonParser.toJsonString(body);
        }

        httpExchange.sendResponseHeaders(httpStatusCode, resContent.getBytes(StandardCharsets.UTF_8).length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(resContent.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        logger.logResponse(resBody);
    }

    private BaseResponse proceed(String reqMethod, String reqPath, Task reqBody) {
        if (reqMethod.equals(HttpMethod.GET.getMethod())
                && PathChecker.isMatchedPath(reqPath, "/tasks")) {
            return todoController.getTodos();
        }

        if (reqMethod.equals(HttpMethod.GET.getMethod())
                && PathChecker.isMatchedPath(reqPath, "/tasks/{id}")) {
            return todoController.getTodo(PathChecker.getPathVariable(reqPath, 2));
        }

        if (reqMethod.equals(HttpMethod.POST.getMethod())
                && PathChecker.isMatchedPath(reqPath, "/tasks")) {
            return todoController.postTodo(reqBody);
        }

        if ((reqMethod.equals(HttpMethod.PUT.getMethod()) || reqMethod.equals(HttpMethod.PATCH.getMethod()))
                && PathChecker.isMatchedPath(reqPath, "/tasks/{id}")) {
            return todoController.editTask(PathChecker.getPathVariable(reqPath, 2), reqBody);
        }

        if (reqMethod.equals(HttpMethod.DELETE.getMethod())
                && PathChecker.isMatchedPath(reqPath, "/tasks/{id}")) {
            return todoController.deleteTodo(PathChecker.getPathVariable(reqPath, 2));
        }

        return new BaseResponse<>(HttpStatusCode.BAD_REQUEST);
    }

}
