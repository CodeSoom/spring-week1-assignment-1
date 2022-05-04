package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.codesoom.assignment.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHttpHandler implements HttpHandler {

    private TaskManager taskManager = new TaskManager();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task task = setTask(path, requestBody);

        String content = "";
        int resCode = HttpStatus.OK;
        List<Task> result = new ArrayList<>();
        if (isTask(path)) {
            switch (method) {
                case HttpMethodCode.GET:
                    if (hasTaskId(path)) {
                        result.add(taskManager.findTask(task));
                        content = JsonUtils.taskToJson(result);
                        resCode = HttpStatus.OK;
                    } else {
                        content = JsonUtils.taskToJson(taskManager.findTaskAll());
                        resCode = HttpStatus.OK;
                    }
                    break;
                case HttpMethodCode.POST:
                    taskManager.insertTask(task);
                    content = JsonUtils.taskToJson(result);
                    resCode = HttpStatus.CREATED;
                    break;
                case HttpMethodCode.PUT:
                case HttpMethodCode.PATCH:
                    if (!hasTaskId(path) || taskManager.findTask(task).isEmpty()) {
                        resCode = HttpStatus.NOT_FOUND;
                    } else {
                        taskManager.updateTask(task);
                        resCode = HttpStatus.OK;
                    }
                    break;
                case HttpMethodCode.DELETE:
                    if (!hasTaskId(path) || taskManager.findTask(task).isEmpty()) {
                        resCode = HttpStatus.NOT_FOUND;
                    } else {
                        taskManager.deleteTask(task);
                        resCode = HttpStatus.NO_CONTENT;
                    }
                    break;
                default:
                    resCode = HttpStatus.NOT_FOUND;
                    break;
            }
        }

        exchange.sendResponseHeaders(resCode, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
    }

    private Task setTask(String path, String requestBody) throws JsonProcessingException {
        Task task = new Task();
        if (!requestBody.isBlank()) {
            task = JsonUtils.jsonToTask(requestBody);
        }

        if (hasTaskId(path)) {
            task.setId(Long.valueOf(path.split("/")[2]));
        }

        return task;
    }

    private boolean isTask(String path) {
        return path.startsWith("/tasks");
    }

    private boolean hasTaskId(String path) {
        return path.split("/").length > 2;
    }


}
