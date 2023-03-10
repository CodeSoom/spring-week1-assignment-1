package com.codesoom.assignment;

import com.codesoom.assignment.config.TaskNotFoundException;
import com.codesoom.assignment.config.UnsupportedMethod;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;

public class DemoHandler implements HttpHandler {

    ObjectMapper objectMapper = new ObjectMapper();
    TaskList taskList = new TaskList();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] pathSegments = createPathSegments(exchange);

        HttpResponse httpResponse = new HttpResponse("", 200);
        if (isTasksRequest(pathSegments)) {
            if (isTasksRequest(pathSegments)) {
                try {
                    httpResponse = fetchHttpResponse(exchange);
                } catch (TaskNotFoundException e) {
                    sendResponse(exchange, new HttpResponse("", 404));
                } catch (UnsupportedMethod e) {
                    sendResponse(exchange, new HttpResponse("", 404));
                }
            }
        }
        sendResponse(exchange, httpResponse);
    }

    private int getTaskPathVariable(String[] pathSegments) {
        if (isNumeric(pathSegments[2])) {
            String taskIdStr = pathSegments[2];
            return Integer.parseInt(taskIdStr);
        }
        throw new InvalidParameterException();
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    private String[] createPathSegments(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        return path.split("/");
    }


    private boolean isTasksRequest(String[] pathSegments) {
        return pathSegments.length >= 2 && pathSegments[1].equals("tasks");
    }

    private HttpResponse fetchHttpResponse(HttpExchange exchange) throws IOException, TaskNotFoundException, UnsupportedMethod {
        String requestMethod = exchange.getRequestMethod();
        String[] pathSegments = createPathSegments(exchange);

        HttpResponse httpResponse;
        switch (requestMethod) {
            case "GET":
                httpResponse = fetchTask(pathSegments);
                break;
            case "POST":
                httpResponse = insertTask(createBody(exchange));
                break;
            case "PUT":
                httpResponse = updateTask(pathSegments, createBody(exchange));
                break;
            case "DELETE":
                httpResponse = deleteTask(pathSegments);
                break;
            default:
                throw new UnsupportedMethod("지원하지 않는 메서드 입니다. 메서드: " + requestMethod);
        }
        return httpResponse;
    }

    private HttpResponse deleteTask(String[] pathArray) throws TaskNotFoundException {
        if (pathArray.length == 3) {
            int requestTaskId = getTaskPathVariable(pathArray);
            if (taskList.delete(requestTaskId)) {
                return new HttpResponse("", 200);
            } else {
                return HttpResponse.failResponse();
            }
        }
        return HttpResponse.failResponse();
    }

    private HttpResponse fetchTask(String[] pathArray) throws IOException, TaskNotFoundException {
        String content = "";
        int httpCode = 200;
        if (pathArray.length == 2) {
            content = createTasksListResponse();
        } else if (pathArray.length == 3) {
            int requestTaskId = getTaskPathVariable(pathArray);
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

    private HttpResponse updateTask(String[] pathArray, String title) throws IOException, TaskNotFoundException {
        if (pathArray.length == 3) {
            int requestTaskId = getTaskPathVariable(pathArray);
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
