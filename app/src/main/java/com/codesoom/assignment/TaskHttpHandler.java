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
    private static final int VALID_PATH_LENGTH_WITHOUT_ID = 2;
    private static final int VALID_PATH_LENGTH_WITH_ID = 3;
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

        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#split(java.lang.String)
        // "/tasks/1" is split into { "", "tasks", "1" }    => length 3
        // "/tasks/" is split into { "", "tasks" }          => length 2
        // "/tasks" is split into { "", "tasks" }           => length 2
        String[] pathItems = path.split("/");

        if (!isValidPath(pathItems)) {
            System.out.println("Invalid path...");
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            return;
        }

        int id = 0;
        if (pathItems.length == VALID_PATH_LENGTH_WITH_ID) {
            // if id is not integer, 400 BadRequest - NumberFormatException thrown
            id = Integer.parseInt(pathItems[VALID_PATH_LENGTH_WITH_ID - 1]);
        }

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "";
        HttpStatusCode httpStatusCode = HttpStatusCode.NotFound;

        if (method.equals("GET")) {
            get(exchange, pathItems.length, id);
            return;
        }

        if (method.equals("POST")) {
            post(exchange, pathItems.length, body);
            return;
        }

        if (method.equals("PUT")) {
            put(exchange, pathItems.length, id, body);
            return;
        }

        if (method.equals("PATCH")) {
            patch(exchange, pathItems.length, id, body);
            return;
        }

        if (method.equals("DELETE")) {
            delete(exchange, pathItems.length, id);
        }

        sendHttpResponse(exchange, httpStatusCode, content);
    }

    private void delete(HttpExchange exchange, int length, int id) throws IOException {
        if (length == VALID_PATH_LENGTH_WITHOUT_ID) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[DELETE] NotFound Exception thrown...");
            return;
        }

        Task task = getTask(id);
        boolean isDeleted = deleteTask(task);

        if (!isDeleted) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[DELETE] NotFound Exception thrown...");
            return;
        }

        sendHttpResponse(exchange, HttpStatusCode.NoContent, "");
        System.out.println("[DELETE] A Task successfully deleted.");
    }

    private void patch(HttpExchange exchange, int length, int id, String body) throws IOException {
        if (length == VALID_PATH_LENGTH_WITHOUT_ID) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[PATCH] NotFound Exception thrown...");
            return;
        }

        Task task = getTask(id);
        Task newTask = toTask(body);
        boolean isPatched = patchTask(task, newTask);

        if (!isPatched) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[PATCH] NotFound Exception thrown...");
            return;
        }

        String taskJson = taskToJSON(newTask);
        sendHttpResponse(exchange, HttpStatusCode.OK, taskJson);
        System.out.println("[PATCH] A Task successfully patched.\n" + taskJson);
    }

    private void put(HttpExchange exchange, int length, int id, String body) throws IOException {
        if (length == VALID_PATH_LENGTH_WITHOUT_ID) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[PUT] NotFound Exception thrown...");
            return;
        }

        Task task = getTask(id);
        Task newTask = toTask(body);
        boolean isUpdated = updateTask(task, newTask);

        if (!isUpdated) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[PUT] NotFound Exception thrown...");
            return;
        }

        String taskJson = taskToJSON(newTask);
        sendHttpResponse(exchange, HttpStatusCode.OK, taskJson);
        System.out.println("[PUT] A Task successfully updated.\n" + taskJson);
    }

    private void post(HttpExchange exchange, int length, String body) throws IOException {
        if (length == VALID_PATH_LENGTH_WITH_ID) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[POST] NotFound Exception thrown...");
            return;
        }

        Task task = toTask(body);
        boolean isAdded = addTask(task);

        if (!isAdded) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[POST] NotFound Exception thrown...");
            return;
        }

        String taskJson = taskToJSON(task);
        sendHttpResponse(exchange, HttpStatusCode.Created, taskJson);
        System.out.println("[POST] A Task successfully added.\n" + taskJson);
    }

    private void get(HttpExchange exchange, int length, int id) throws IOException {
        if (length == VALID_PATH_LENGTH_WITHOUT_ID) {
            String tasksJson = tasksToJSON();
            sendHttpResponse(exchange, HttpStatusCode.OK, tasksJson);
            System.out.println("[GET] Tasks successfully returned.\n" + tasksJson);
            return;
        }

        Task task = getTask(id);

        if (task == null) {
            sendHttpResponse(exchange, HttpStatusCode.NotFound, "");
            System.out.println("[GET] NotFound Exception thrown...\nTask #" + id + " doesn't exist");
            return;
        }

        String taskJson = taskToJSON(task);

        sendHttpResponse(exchange, HttpStatusCode.OK, taskJson);
        System.out.println("[GET] A Task successfully returned.\n" + taskJson);
    }

    private void sendHttpResponse(HttpExchange exchange, HttpStatusCode httpStatusCode, String content) throws IOException {
        exchange.sendResponseHeaders(httpStatusCode.getValue(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private boolean deleteTask(Task task) {
        if (task == null) {
            return false;
        }

        tasks.remove(task);
        return true;
    }

    private boolean patchTask(Task task, Task newTask) throws JsonProcessingException {
        if (task == null) {
            return false;
        }
        if (newTask == null) {
            return false;
        }

        if (!task.getTitle().equals(newTask.getTitle())) {
            task.setTitle(newTask.getTitle());
        }

        return true;
    }

    private boolean updateTask(Task task, Task newTask) throws JsonProcessingException {
        if (task == null) {
            return false;
        }
        if (newTask == null) {
            return false;
        }

        if (newTask.getTitle() == null) {
            return false;
        }

        task.setTitle(newTask.getTitle());

        return true;
    }

    private boolean addTask(Task task) throws JsonProcessingException {
        if (task == null) {
            return false;
        }

        if (task.getId() == null) {
            long id = getNextId();
            task.setId(id);
        }

        if (task.getId() < getNextId()) {
            return false;
        }

        tasks.add(task);
        return true;
    }

    private long getNextId() {
        if (tasks.isEmpty()) {
            return 1L;
        }

        int size = tasks.size();
        long lastId = tasks.get(size - 1).getId();
        return lastId + 1;
    }

    public boolean isValidPath(String[] pathItems) {
        if (pathItems.length != VALID_PATH_LENGTH_WITHOUT_ID && pathItems.length != VALID_PATH_LENGTH_WITH_ID) {
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
