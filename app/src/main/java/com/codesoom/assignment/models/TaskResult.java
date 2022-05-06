package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpStatus;
import com.codesoom.assignment.util.JsonUtils;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskResult {
    private List<Task> tasks;
    private String taskJson;
    private int httpStatus;

    public TaskResult(List<Task> tasks, int httpStatus) {
        this.tasks = tasks;
        this.httpStatus = httpStatus;
        try {
            this.taskJson = tasks == null ? "" : JsonUtils.taskToJson(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertTaskToJson() {
        return this.taskJson;
    }

    public void configureResponseHeaders(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(this.httpStatus, taskJson.getBytes().length);
    }
}
