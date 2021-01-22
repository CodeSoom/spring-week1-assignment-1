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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        if (path == null) {
            System.out.println("Undefined path...");
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            return;
        }

        if (path.equals("/")) {
            System.out.println("Default path for checking server is running...");
            sendHttpResponse(exchange, HttpStatusCode.OK, "");
            return;
        }

        // "/tasks/1" is split into { "", "tasks", "1" }
        String[] pathItems = path.split("/");

        if (!isValidPath(pathItems)) {
            System.out.println("Invalid path...");
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            return;
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

        String content = "";
        HttpStatusCode httpStatusCode = HttpStatusCode.NotFound;

        // GET all tasks
        if (method.equals("GET") && (pathItems.length == 2)) {
            content = tasksToJSON();
            httpStatusCode = HttpStatusCode.OK;
            System.out.println("[GET] Tasks successfully returned.\n" + content);
            sendHttpResponse(exchange, httpStatusCode, content);
            return;
        }

        // GET one task
        if (method.equals("GET") && (pathItems.length == 3)) {
            Task task = getTask(id);

            if (task == null) {
                httpStatusCode = HttpStatusCode.NotFound;
                System.out.println("[GET] NotFound Exception thrown...\nTask #" + id + " doesn't exist");
            } else {
                content = taskToJSON(task);
                httpStatusCode = HttpStatusCode.OK;
                System.out.println("[GET] A Task successfully returned.\n" + content);
            }

            sendHttpResponse(exchange, httpStatusCode, content);
            return;
        }

        // POST
        if (method.equals("POST") && (pathItems.length == 2)) {
            Task task = toTask(body);
            HttpStatusCode result = addTask(task);
            httpStatusCode = result;

            switch (result) {
                case Created:
                    content = taskToJSON(task);
                    System.out.println("[POST] A Task successfully added.\n" + content);
                    break;
                case NotFound:
                    System.out.println("[POST] NotFound Exception thrown...");
                    break;
                default:
                    System.out.println("[POST] Unhandled Exception thrown...");
                    break;
            }

            sendHttpResponse(exchange, httpStatusCode, content);
            return;
        }

        // PUT
        if (method.equals("PUT") && (pathItems.length == 3)) {
            Task task = getTask(id);
            Task newTask = toTask(body);
            HttpStatusCode result = updateTask(task, newTask);
            httpStatusCode = result;

            switch (result) {
                case OK:
                    content = taskToJSON(newTask);
                    System.out.println("[PUT] Task successfully updated.\n" + content);
                    break;
                case NotFound:
                    System.out.println("[PUT] NotFound Exception thrown...");
                    break;
                default:
                    System.out.println("[PUT] Unhandled Exception thrown...");
                    break;
            }

            sendHttpResponse(exchange, httpStatusCode, content);
            return;
        }

        // PATCH
        if (method.equals("PATCH") && (pathItems.length == 3)) {
            Task task = getTask(id);
            Task newTask = toTask(body);
            HttpStatusCode result = patchTask(task, newTask);
            httpStatusCode = result;

            switch (result) {
                case OK:
                    content = taskToJSON(newTask);
                    System.out.println("[PATCH] A Task successfully patched.\n" + content);
                    break;
                case NotFound:
                    System.out.println("[PATCH] NotFound Exception thrown...");
                    break;
                default:
                    System.out.println("[PATCH] Unhandled Exception thrown...");
                    break;
            }

            sendHttpResponse(exchange, httpStatusCode, content);
            return;
        }

        // DELETE
        if (method.equals("DELETE") && (pathItems.length == 3)) {
            Task task = getTask(id);
            HttpStatusCode result = deleteTask(task);
            httpStatusCode = result;

            switch (result) {
                case NoContent:
                    System.out.println("[DELETE] A Task successfully deleted.");
                    break;
                case NotFound:
                    System.out.println("[DELETE] NotFound Exception thrown...");
                    break;
                default:
                    System.out.println("[DELETE] Unhandled Exception thrown...");
                    break;
            }

            sendHttpResponse(exchange, httpStatusCode, content);
            return;
        }

        sendHttpResponse(exchange, httpStatusCode, content);
    }

    private void sendHttpResponse(HttpExchange exchange, HttpStatusCode httpStatusCode, String content) throws IOException {
        exchange.sendResponseHeaders(httpStatusCode.getValue(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private HttpStatusCode deleteTask(Task task) {
        if (task == null) {
            return HttpStatusCode.NotFound;
        }

        tasks.remove(task);
        return HttpStatusCode.NoContent;
    }

    private HttpStatusCode patchTask(Task task, Task newTask) throws JsonProcessingException {
        if (task == null) {
            return HttpStatusCode.NotFound;
        }
        if (newTask == null) {
            return HttpStatusCode.NotFound;
        }

        if (!task.getTitle().equals(newTask.getTitle())) {
            task.setTitle(newTask.getTitle());
        }

        return HttpStatusCode.OK;
    }

    private HttpStatusCode updateTask(Task task, Task newTask) throws JsonProcessingException {
        if (task == null) {
            return HttpStatusCode.NotFound;
        }
        if (newTask == null) {
            return HttpStatusCode.NotFound;
        }

        if (newTask.getTitle() == null) {
            return HttpStatusCode.NotFound;
        }

        task.setTitle(newTask.getTitle());

        return HttpStatusCode.OK;
    }

    private HttpStatusCode addTask(Task task) throws JsonProcessingException {
        if (task == null) {
            return HttpStatusCode.NotFound;
        }

        if (task.getId() == null) {
            long id = getNextId();
            task.setId(id);
        }

        if (task.getId() < getNextId()) {
            return HttpStatusCode.NotFound;
        }

        tasks.add(task);
        return HttpStatusCode.Created;
    }

    private long getNextId() {
        if (tasks.isEmpty()) {
            return 1L;
        }

        int size = tasks.size();
        long lastId = tasks.get(size - 1).getId();
        return lastId + 1;
    }

    private boolean isValidPath(String[] pathItems) {
        if (pathItems.length < 2) {
            return false;
        }
        if (pathItems.length > 3) {
            return false;
        }
        if (!pathItems[0].isBlank()) {
            return false;
        }
        if (!pathItems[1].equals(MAIN_PATH)) {
            return false;
        }

        return true;
    }

    private Task getTask(int id) throws IOException {
        Optional<Task> task = tasks.stream().filter(i -> i.getId() == id).findFirst();

        if (task.isEmpty()) return null;
        return task.get();
    }

    private Task toTask(String content) throws JsonProcessingException {
        if (content.isBlank()) {
            return null;
        }
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
