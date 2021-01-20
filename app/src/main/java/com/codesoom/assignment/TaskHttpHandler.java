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
        int httpStatusCode = HttpStatusCode.OK;

        if (path == null) {
            System.out.println("[Undefined Path] 404 NotFound Exception");
            content = "Undefined path";
            httpStatusCode = HttpStatusCode.NotFound;
        }

        // "/tasks/1" is split into { "", "tasks", "1" }
        String[] pathItems = path.split("/");

        if (!isValidPath(pathItems)) {
            System.out.println("[Invalid PATH] 404 NotFound Exception");
            content = "Invalid path";
            httpStatusCode = HttpStatusCode.NotFound;
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

        if (httpStatusCode != HttpStatusCode.OK) {
            exchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            return;
        }

        httpStatusCode = HttpStatusCode.NotFound;
        content = "";

        // GET all tasks
        if (method.equals("GET") && (pathItems.length == 2)) {
            content = tasksToJSON();
            httpStatusCode = HttpStatusCode.OK;
        }

        // GET one task
        if (method.equals("GET") && (pathItems.length == 3)) {
            Task task = getTask(id);
            httpStatusCode = HttpStatusCode.OK;
            content = taskToJSON(task);

            if (task == null) {
                httpStatusCode = HttpStatusCode.NotFound;
                content = "Task #" + id +" doesn't exist";
            }
        }

        // POST
        if (method.equals("POST") && (pathItems.length == 2)) {
            Task task = toTask(body);
            int result = addTask(task);
            httpStatusCode = result;

            switch (result) {
                case HttpStatusCode.OK:
                    content = taskToJSON(task);
                    break;
                case HttpStatusCode.NotFound:
                    content = "Cannot add a Task";
                    break;
                default:
                    content = "";
            }
        }

        // PUT
        if (method.equals("PUT") && (pathItems.length == 3)) {
            Task task = getTask(id);
            Task newTask = toTask(body);
            int result = updateTask(task, newTask);
            httpStatusCode = result;

            switch (result) {
                case HttpStatusCode.OK:
                    content = taskToJSON(newTask);
                    break;
                default:
                    break;
            }
        }

        // PATCH
        if (method.equals("PATCH") && (pathItems.length == 3)) {
            Task task = getTask(id);
            Task newTask = toTask(body);
            int result = patchTask(task, newTask);
            httpStatusCode = result;

            switch (result) {
                case HttpStatusCode.OK:
                    content = taskToJSON(newTask);
                    break;
                default:
                    break;
            }
        }

        // DELETE
        if (method.equals("DELETE") && (pathItems.length == 3)) {
            Task task = getTask(id);
            int result = deleteTask(task);
            httpStatusCode = result;
        }

        exchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private int deleteTask(Task task) {
        if (task == null) return HttpStatusCode.NotFound;

        tasks.remove(task);
        return HttpStatusCode.OK;
    }

    private int patchTask(Task task, Task newTask) throws JsonProcessingException {
        if (task == null) return HttpStatusCode.NotFound;
        if (newTask == null) return HttpStatusCode.NotFound;

        if (task.getTitle() != newTask.getTitle()) {
            task.setTitle(newTask.getTitle());
        }

        return HttpStatusCode.OK;
    }

    private int updateTask(Task task, Task newTask) throws JsonProcessingException {
        if (task == null) return HttpStatusCode.NotFound;
        if (newTask == null) return HttpStatusCode.NotFound;

        if (newTask.getTitle() == null) return HttpStatusCode.NotFound;

        task.setTitle(newTask.getTitle());

        return HttpStatusCode.OK;
    }

    private int addTask(Task task) throws JsonProcessingException {
        if (task == null) return HttpStatusCode.NotFound;

        if (task.getId() == null) {
            long id = getNextId();
            task.setId(id);
        }

        if (task.getId() < getNextId()) return HttpStatusCode.NotFound;

        tasks.add(task);
        return HttpStatusCode.OK;
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
        if (content.isBlank()) return null;
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
