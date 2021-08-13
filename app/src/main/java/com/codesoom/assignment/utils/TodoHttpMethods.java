package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.StatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskIdGenerator;
import com.codesoom.assignment.models.Title;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.getId;
import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.writeOutputStream;
import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.toTitle;
import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.toTask;
import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.taskToJSON;
import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.tasksToJSON;

public class TodoHttpMethods {

    private static final String URI_WITHOUT_PARAMETERS = "/tasks";
    private String content = "";

    public void handleDeleteMethod(String path, HttpExchange exchange, Map<Long, Task> taskMap) throws IOException {
        Long id = getId(path);
        Task task = taskMap.get(id);

        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }

        taskMap.remove(id);
        content = "";
        writeOutputStream(exchange, content, StatusCode.NO_CONTENT);
    }

    public void handlePatchMethod(String path, HttpExchange exchange, String body, Map<Long, Task> taskMap) throws IOException {
        Long id = getId(path);
        Task task = taskMap.get(id);

        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }
        Title title = toTitle(body);
        task.setTitle(title.getTitle());
        taskMap.put(id, task);

        content = taskToJSON(task);
        writeOutputStream(exchange, content, StatusCode.OK);
    }

    public void handlePutMethod(String path, HttpExchange exchange, String body, Map<Long, Task> taskMap) throws IOException {
        Long id = getId(path);
        Task task = taskMap.get(id);
        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }

        Task changeTask = toTask(body);
        task.setTitle(changeTask.getTitle());
        taskMap.put(id, task);

        content = taskToJSON(task);
        writeOutputStream(exchange, content, StatusCode.OK);
    }

    public void handlePostMethodWithParameter(HttpExchange exchange, String body, Map<Long, Task> taskMap) throws IOException {
        Task task = toTask(body);
        taskMap.put(task.getId(), task);

        Long lastSequence = TaskIdGenerator.getLastSequence();
        Task lastTask = taskMap.get(lastSequence);

        content = taskToJSON(lastTask);
        writeOutputStream(exchange, content, StatusCode.Created);
    }

    public void handleBasicGetMethod(String path, HttpExchange exchange, Map<Long, Task> taskMap) throws IOException {
        if (URI_WITHOUT_PARAMETERS.equals(path)) {
            content = tasksToJSON(taskMap);
            writeOutputStream(exchange, content, StatusCode.OK);
            return;
        }

        Long id = getId(path);
        Task task = taskMap.get(id);

        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }

        content = taskToJSON(task);
        writeOutputStream(exchange, content, StatusCode.OK);
    }
}
