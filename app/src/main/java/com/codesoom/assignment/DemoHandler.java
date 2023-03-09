package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHandler implements HttpHandler {

    ObjectMapper objectMapper = new ObjectMapper();
    TaskList taskList = new TaskList();

    public static final String slash = "/";
    public static final String GET = "GET";


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] pathArray = createPathArray(exchange);

        HttpResponse httpResponse = new HttpResponse("", 200);
        try {
            if (isTasksRequest(pathArray)) {
                httpResponse = fetchHttpResponse(exchange);
            }
        } catch (NullPointerException e) {
            httpResponse = new HttpResponse("", 404);
        } finally {
            sendResponse(exchange, httpResponse);
        }
    }

    private int getRequestTaskId(String[] pathArray) {
        String taskIdStr = pathArray[2];
        return Integer.parseInt(taskIdStr);
    }

    private String[] createPathArray(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        return path.split(slash);
    }


    private boolean isTasksRequest(String[] pathArray) {
        return pathArray.length >= 2 && pathArray[1].equals("tasks");
    }

    private HttpResponse fetchHttpResponse(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] pathArray = createPathArray(exchange);

        HttpResponse httpResponse;
        switch (requestMethod) {
            case "GET":
                httpResponse = fetchTask(pathArray);
                break;
            case "POST":
                httpResponse = insertTask(createBody(exchange));
                break;
            case "PUT":
                httpResponse = updateTask(pathArray, createBody(exchange));
                break;
            default:
                httpResponse = deleteTask(pathArray);
        }
        return httpResponse;
    }

    private HttpResponse deleteTask(String[] pathArray) {
        if (pathArray.length == 3) {
            int requestTaskId = getRequestTaskId(pathArray);
            if (taskList.delete(requestTaskId)) {
                return new HttpResponse("", 200);
            } else {
                return HttpResponse.failResponse();
            }
        }
        return HttpResponse.failResponse();
    }

    private HttpResponse fetchTask(String[] pathArray) throws IOException {
        String content = "";
        int httpCode = 200;
        if (pathArray.length == 2) {
            content = createTasksListResponse();
        } else if (pathArray.length == 3) {
            int requestTaskId = getRequestTaskId(pathArray);
            if (requestTaskId >= 1 && requestTaskId <= taskList.size()) {
                Task task = taskList.get(requestTaskId);
                content = taskToJson(task);
            } else {
                httpCode = 404;
            }
        }
        return new HttpResponse(content, httpCode);
    }

    private HttpResponse insertTask(String title) throws IOException {
        if (!title.isBlank()) {
            Task task = addTask(title);
            return new HttpResponse(taskToJson(task), 201);
        }
        return new HttpResponse("", 404);
    }

    private HttpResponse updateTask(String[] pathArray, String title) throws IOException {
        if (pathArray.length == 3) {
            int requestTaskId = getRequestTaskId(pathArray);
            Task requestTask = toTask(title);
            taskList.updateTask(requestTaskId, requestTask);
            return new HttpResponse(taskToJson(taskList.get(requestTaskId)), 200);
        }
        return new HttpResponse("", 404);
    }


    private String createTasksListResponse() throws IOException {
        String content;
        if (taskList.size() == 0) {
            content = "[]";
        } else {
            content = taskToJson();
        }
        return content;
    }

    private static String createBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    private String taskToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskList.getItems());
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private Task addTask(String title) throws JsonProcessingException {
        Task task = toTask(title);
        taskList.add(task);
        return task;
    }

    private static void sendResponse(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        exchange.sendResponseHeaders(httpResponse.getHttpCode(), httpResponse.getContent().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
