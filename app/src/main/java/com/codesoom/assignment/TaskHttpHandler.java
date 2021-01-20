package com.codesoom.assignment;

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

class HttpStatusCode {
    public static final int OK = 200;
    public static final int BadRequest = 400;
    public static final int NotFound = 404;
}

public class TaskHttpHandler implements HttpHandler {
    private static final String MAIN_PATH = "tasks";
    private List<Task> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method + " " + path);

        String content = "OK";
        int code = HttpStatusCode.OK;

        if (path == null) {
            System.out.println("[Undefined Path] 404 NotFound Exception");
            content = "Undefined path";
            code = HttpStatusCode.NotFound;
        }

        // "/tasks/1" is split into { "", "tasks", "1" }
        String[] pathItems = path.split("/");

        if (!isValidPath(pathItems)) {
            System.out.println("[Invalid PATH] 404 NotFound Exception");
            content = "Invalid path";
            code = HttpStatusCode.NotFound;
        }

        int id = 0;
        if (pathItems.length == 3) {
            // if segments[2] is not integer, 400 BadRequest - NumberFormatException thrown
            id = Integer.parseInt(pathItems[2]);
        }

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (code != HttpStatusCode.OK) {
            exchange.sendResponseHeaders(code, content.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            return;
        }

        // GET all tasks
        if (method.equals("GET") && (pathItems.length == 2)) {
            content = taskToJSON();
        }

        // GET one task
        if (method.equals("GET") && (pathItems.length == 3)) {
            Task task = getTask(id);
            content = taskToJSON(task);

            if (task == null) {
                System.out.println("[GET] 404 NotFound Exception");
                content = "Task #" + id +" doesn't exist";
                code = HttpStatusCode.NotFound;
            }
        }

        // POST
        if (method.equals("POST")) {
            if (body.isBlank() || (pathItems.length != 2)) {
                // 400 BadRequest Exception
                System.out.println("[POST] 400 BadRequest Exception");
                content = "POST failed";
                code = HttpStatusCode.BadRequest;
            }
            else {
                Task task = toTask(body);

                if (tasks.isEmpty()) {
                    if(task.getId() == null) task.setId(1L);
                }
                else {
                    int size = tasks.size();
                    Long lastId = tasks.get(size - 1).getId();

                    if (task.getId() == null) {
                        task.setId(lastId + 1);
                    }
                    else {
                        if (task.getId() <= lastId) {
                            // 400 BadRequest Exception
                            System.out.println("[POST] 400 BadRequest Exception");
                            exchange.sendResponseHeaders(400, 0);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.flush();
                            outputStream.close();
                            return;
                        }
                    }
                }

                tasks.add(task);
                content = "Task successfully added.";
            }
        }

        if (method.equals("PUT")) {

        }

        if (method.equals("PATCH")) {

        }

        if (method.equals("DELETE")) {

        }

        exchange.sendResponseHeaders(code, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private boolean isValidPath(String[] pathItems) {
        if (pathItems.length < 2) return false;
        if (pathItems.length > 3) return false;
        if (!pathItems[0].isBlank()) return false;
        if (!pathItems[1].equals(MAIN_PATH)) return false;

        return true;
    }

    private Task getTask(int id) throws IOException {
        Task foundTask = null;

        for (Task task : tasks) {
            if(task.getId() == id) {
                foundTask = task;
                break;
            }
        }
        return foundTask;
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String taskToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
