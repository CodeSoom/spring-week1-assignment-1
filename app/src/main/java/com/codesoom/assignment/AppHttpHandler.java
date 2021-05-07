package com.codesoom.assignment;

import com.codesoom.assignment.exception.NotFoundTaskIdException;
import com.codesoom.assignment.model.AppResponseEntity;
import com.codesoom.assignment.model.HttpMethod;
import com.codesoom.assignment.model.HttpStatusCode;
import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

import java.net.URI;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


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
        final int taskIdIdx = 2;
        String uriPath = uri.getPath();
        String[] uriPaths = uriPath.split("/");
        String rootPath = uriPaths[1];
        String taskId = "";
        String contents = "";
        if (uriPaths.length > taskIdIdx) {
            taskId = uriPaths[taskIdIdx];
        }

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        logger.info("method : " + method + " /  uriPath : " + uriPath + " / body : " + body);

        HttpStatusCode httpStatusCode = HttpStatusCode.OK;
        try {
            if (taskPath.equals(rootPath)) {
                AppResponseEntity appResponseEntity = requestProcessor(method, body, taskId);
                contents = appResponseEntity.getContents();
                httpStatusCode = appResponseEntity.getHttpStatusCode();
            }
        } catch (NotFoundTaskIdException ne) {
            httpStatusCode = HttpStatusCode.NOT_FOUND;
        }


        exchange.sendResponseHeaders(httpStatusCode.getStatusCode(), contents.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(contents.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private AppResponseEntity requestProcessor(String method, String body, String taskId) throws IOException {
        String contents = "";
        HttpStatusCode httpStatusCode = HttpStatusCode.OK;

        HttpMethod httpMethod = HttpMethod.valueOf(method);

        switch (httpMethod) {
            case GET:
                contents = tasksToJSONByPath(taskId);
                httpStatusCode = HttpStatusCode.OK;
                break;
            case POST:
                if (!body.isBlank()) {
                    Task task = toTask(body);
                    task.setId(getTaskMaxId());
                    tasks.add(task);
                    httpStatusCode = HttpStatusCode.CREATED;
                    contents = tasksToJSON(task);
                }
                break;
            case DELETE:
                deleteTask(taskId);
                httpStatusCode = HttpStatusCode.NO_CONTENT;
                break;
            case PUT:
                contents = modifyTask(taskId, body);
                httpStatusCode = HttpStatusCode.OK;
                break;
            default:
                break;
        }
        return new AppResponseEntity(httpStatusCode, contents);
    }

    private String modifyTask(String taskId, String body) throws IOException {

        String contents = "";
        if (!"".equals(taskId)) {
            return " ";
        }
        Task findTask = findTask(taskId);
        int tasksIdx = tasks.indexOf(findTask);
        Task task = toTask(body);
        task.setId(findTask.getId());
        tasks.set(tasksIdx, task);
        contents = tasksToJSON(task);
        return contents;
    }

    private Task findTask(String taskId) {
        Long longTaskId = Long.parseLong(taskId);
        return tasks.stream()
                .filter(t -> t.getId() == longTaskId)
                .findFirst()
                .orElseThrow(NotFoundTaskIdException::new);
    }


    private void deleteTask(String taskId) {
        if (!"".equals(taskId)) {
            return;
        }
        Task task = findTask(taskId);
        tasks.remove(task);
    }

    private String tasksToJSONByPath(String taskId) throws IOException {
        String contents = "";
        if (!"".equals(taskId)) {
            Task findTask = findTask(taskId);
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
            maxId = tasks.stream()
                    .max(comparator)
                    .orElseThrow(NotFoundTaskIdException::new)
                    .getId() + 1;
        }
        return maxId;
    }
}
