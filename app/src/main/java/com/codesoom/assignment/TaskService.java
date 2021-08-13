package com.codesoom.assignment;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.NoSuchElementException;

public class TaskService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TaskManager taskManager = new TaskManager();

    public Response create(String body) {
        String content;
        int statusCode;

        try {
            String title = convertJsonToTask(body).getTitle();
            taskManager.create(title);
            content = convertTaskToJson(taskManager.getLast());
            statusCode = 201;
        } catch (IOException e) {
            content = e.toString();
            statusCode = 500;
        }

        return new Response(statusCode, content);
    }

    public Response getAll() {
        String content;
        int statusCode;

        try {
            content = convertTasksToJson(taskManager.getAll());
            statusCode = 200;
        } catch (IOException e) {
            content = e.toString();
            statusCode = 500;
        }

        return new Response(statusCode, content);
    }

    public Response getOne(Path path) {
        String content;
        int statusCode;

        try {
            Long id = path.getIdOf("tasks");
            Task resultTask = taskManager.getOne(id);
            content = convertTaskToJson(resultTask);
            statusCode = 200;
        } catch (NoSuchElementException | IOException e) {
            content = e.toString();
            statusCode = 404;
        }

        return new Response(statusCode, content);
    }

    public Response remove(Path path) throws NoSuchElementException {
        String content;
        int statusCode;

        try {
            Long id = path.getIdOf("tasks");
            taskManager.remove(id);
            content = "Success Remove Task : " + id;
            statusCode = 204;
        } catch (NoSuchElementException e) {
            content = e.toString();
            statusCode = 404;
        }

        return new Response(statusCode, content);
    }

    public Response update(Path path, String body) {
        String content;
        int statusCode;

        try {
            Long id = path.getIdOf("tasks");
            String title = convertJsonToTask(body).getTitle();
            taskManager.update(id, title);
            content = convertTaskToJson(taskManager.getOne(id));
            statusCode = 200;
        } catch(NoSuchElementException | IOException e) {
            content = e.toString();
            statusCode = 404;
        }

        return new Response(statusCode, content);
    }

    private Task convertJsonToTask(String content) throws JsonProcessingException {
        return this.objectMapper.readValue(content, Task.class);
    }

    private String convertTasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String convertTaskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
