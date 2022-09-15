package com.codesoom.assignment.handler;


import com.codesoom.assignment.enums.HttpStatus;
import com.codesoom.assignment.enums.Method;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.enums.Method.*;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpStatus httpStatus;
    String content = "Hello World!";
    private Long id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Method method = valueOf(exchange.getRequestMethod());
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        addTaskToLists(body);

        methodDispatcher(method, path);

        exchange.sendResponseHeaders(httpStatus.getStatus(), content.getBytes().length);

        sendResponseBody(exchange);
    }

    private void addTaskToLists(String body) throws JsonProcessingException {
        if (!body.isEmpty()) {
            Task task1 = toTask(body);
            task1.setId(++id);
            tasks.add(task1);
        }
    }

    private void sendResponseBody(HttpExchange exchange) throws IOException {
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private void methodDispatcher(Method method, String path) throws IOException {
        switch (method) {
            case GET:

//        GET /tasks/{id}
                getMappingTasks(method, path);
                break;
            case POST:
                //       POST /tasks
                if (method.equals(POST) && path.equals("/tasks")) {

                    content = "Create a new task";
                }
                httpStatus = HttpStatus.CREATED;
                break;

            case DELETE:
                httpStatus = HttpStatus.NOTFOUND;
                break;

            case PATCH:
                httpStatus = HttpStatus.NOTFOUND;
                break;
            case PUT:
                httpStatus = HttpStatus.NOTFOUND;
                break;

            default:

        }
    }

    private void getMappingTasks(Method method, String path) throws IOException {

//        GET /tasks
        if (method.equals(GET) && path.equals("/tasks")) {
            content = tasksToJson();
            httpStatus = HttpStatus.OK;
            return;
        }
//        GET /tasks/{id}
        else if (method.equals("GET") && path.contains("/tasks/")) {
            String[] split = path.split("/");

            String id = split[2];
            if (!isNumeric(id)) {
                System.out.println("tasks id not a number");
                httpStatus = HttpStatus.NOTFOUND;
                return;
            }

            for (Task task : tasks) {
                if (task.getId() == Long.parseLong(id)) {
                    content = taskToJson(task);
                    httpStatus = HttpStatus.OK;
                    return;
                }
            }

            httpStatus = HttpStatus.NOTFOUND;
            content = "";
        }


    }

    private Task toTask(String body) throws JsonProcessingException {
        System.out.println(body);
        return objectMapper.readValue(body, Task.class);
    }

    private String tasksToJson() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }
}
