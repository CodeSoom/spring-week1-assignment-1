package com.codesoom.assignment;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AppHttpHandler implements HttpHandler {
    private final static Logger logger = Logger.getGlobal();
    private final String taskPath = "tasks";
    private List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public AppHttpHandler() {
        logger.setLevel(Level.INFO);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String uriPath = uri.getPath();
        String[] uriPaths = uriPath.split("/");


        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        logger.info("method : " + method + " /  uriPath : " + uriPath + " / body : " + body);
        String contents = "";
        int HttpStatusCode = 200;
        try {
            if ("GET".equals(method) && taskPath.equals(uriPaths[1])) {
                contents = tasksToJSONByPath(uriPaths);
                HttpStatusCode = 200;
            }
            if ("POST".equals(method) && taskPath.equals(uriPaths[1])) {
                if (!body.isBlank()) {
                    Task task = toTask(body);
                    task.setId(getTaskMaxId());
                    tasks.add(task);
                    HttpStatusCode = 201;
                    contents = tasksToJSON(task);
                }
            }
            if ("DELETE".equals(method) && taskPath.equals(uriPaths[1])) {

                deleteTask(uriPaths);
                HttpStatusCode = 204;
            }

            if ("PUT".equals(method) && taskPath.equals(uriPaths[1])) {
                contents = modifyTask(uriPaths, body);
                HttpStatusCode = 200;
            }
        } catch (NoSuchElementException ne) {
            HttpStatusCode = 404;
        }


        exchange.sendResponseHeaders(HttpStatusCode, contents.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(contents.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String modifyTask(String[] uriPaths, String body) throws IOException {

        String contents = "";
        if (uriPaths.length > 2) {
            Task findTask = findTask(uriPaths[2]);
            int tasksIdx = tasks.indexOf(findTask);

            Task task = toTask(body);
            task.setId(findTask.getId());
            tasks.set(tasksIdx, task);
            contents = tasksToJSON(task);

        }
        return contents;
    }

    private Task findTask(String uriPath) {
        return tasks.stream().filter(t -> t.getId() == Long.parseLong(uriPath)).findFirst().orElseThrow(NoSuchElementException::new);
    }


    private void deleteTask(String[] uriPaths) {
        if (uriPaths.length > 2) {
            Task task = findTask(uriPaths[2]);
            tasks.remove(task);
        }
    }

    private String tasksToJSONByPath(String[] uriPaths) throws IOException {
        String contents = "";
        if (uriPaths.length > 2) {
            Task findTask = findTask(uriPaths[2]);
            contents = tasksToJSON(findTask);
        } else {
            contents = tasksToJSON();
        }
        return contents;
    }

    private Task toTask(String contents) throws JsonProcessingException {
        return objectMapper.readValue(contents, Task.class);
    }

    private String tasksToJSON() throws IOException {
        return tasksToJSON(tasks);
    }


    private String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String tasksToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }


    private Long getTaskMaxId() {
        Long maxId = 1L;
        if (tasks.size() > 0) {
            Comparator<Task> comparator = Comparator.comparingLong(Task::getId);
            maxId = tasks.stream().max(comparator).orElseThrow(NoSuchElementException::new).getId() + 1;
        }
        return maxId;
    }
}
