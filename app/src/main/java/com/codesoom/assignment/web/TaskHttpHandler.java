package com.codesoom.assignment.web;

import com.codesoom.assignment.TaskManager;
import com.codesoom.assignment.TaskMapper;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    public static final String NOT_FOUND_MESSAGE = "Not Found.";
    public static final String NOT_FOUND_TASK_ID_MESSAGE = "Can't find task from your id.";

    private final TaskManager taskManager;
    private final TaskMapper taskMapper;

    public TaskHttpHandler() {
        this.taskManager = TaskManager.getInstance();
        this.taskMapper = new TaskMapper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(httpExchange);
        System.out.println(httpRequest);

        InputStream httpRequestBody = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));

        if (httpRequest.isMatchMethod("GET") && httpRequest.isMatchPath("/tasks")) {
            new HttpResponseOK(httpExchange).send(taskMapper.tasksToJson());
        }

        if (httpRequest.isMatchMethod("GET") && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            if (!taskManager.existTaskFromId(taskId)) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            Task content = taskManager.findTaskFromId(taskId);
            new HttpResponseOK(httpExchange).send(taskMapper.taskToJson(content));
        }

        if (httpRequest.isMatchMethod("POST") && httpRequest.isMatchPath("/tasks")) {
            if (!body.isEmpty()) {
                Task task = taskMapper.getTaskFromContent(body);
                Task newTask = taskManager.createTask(task);

                new HttpResponseCreated(httpExchange).send(taskMapper.taskToJson(newTask));
            }
        }

        if (httpRequest.isUpdateMethod() && httpRequest.pathStartsWith("/tasks") && httpRequest
            .hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            if (!taskManager.existTaskFromId(taskId)) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            Task task = taskMapper.getTaskFromContent(body);
            Task updatedTask = taskManager.updateTask(taskId, task);

            new HttpResponseOK(httpExchange).send(taskMapper.taskToJson(updatedTask));
        }

        if (httpRequest.isMatchMethod("DELETE") && httpRequest.pathStartsWith("/tasks")
            && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            if (!taskManager.existTaskFromId(taskId)) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            Task deletedTask = taskManager.deleteTask(taskId);

            new HttpResponseNoContent(httpExchange).send(taskMapper.taskToJson(deletedTask));
        }

        new HttpResponseNotFound(httpExchange).send(NOT_FOUND_MESSAGE);
    }
}
