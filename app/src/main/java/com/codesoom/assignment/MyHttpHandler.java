package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.HttpStatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    List<Task> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    public MyHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();


        String path = requestURI.toString();
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " - " + path);
        if (!requestBody.isBlank()) {
            System.out.println(requestBody);
        }


        String content = "";
        HttpStatusCode httpStatusCode = HttpStatusCode.NOT_FOUND;

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
            httpStatusCode = HttpStatusCode.OK;
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            try {
                Task task = toTask(requestBody);

                long newId = tasks.size() == 0 ? 1 : tasks.get(tasks.size() - 1).getId() + 1;
                task.setId(newId);
                tasks.add(task);

                content = taskToJson(task);

                httpStatusCode = HttpStatusCode.CREATED;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                httpStatusCode = HttpStatusCode.BAD_REQUEST;
            }
        }

        if (method.equals("GET") && path.startsWith("/tasks/")) {
            try {
                String idStr = path.substring(path.lastIndexOf("/") + 1);
                System.out.println(idStr);
                long id = Long.parseLong(idStr);

                for (Task task : tasks) {
                    if (task.getId() == id) {
                        content = taskToJson(task);
                        httpStatusCode = HttpStatusCode.OK;
                        break;
                    }
                }
                if (httpStatusCode != HttpStatusCode.OK)
                    httpStatusCode = HttpStatusCode.NOT_FOUND;

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                httpStatusCode = HttpStatusCode.NOT_FOUND;
            } catch (IOException e) {
                e.printStackTrace();
                httpStatusCode = HttpStatusCode.INTERNAL_SERVER_ERROR;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                httpStatusCode = HttpStatusCode.NOT_FOUND;
            }
        }

        exchange.sendResponseHeaders(httpStatusCode.getValue(), content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.close();
    }

    private Task toTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
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