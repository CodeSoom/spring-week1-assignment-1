package com.codesoom.assignment.method;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class DoTask {
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final List<Task> tasks = new ArrayList<>();
    protected HttpExchange exchange;

    DoTask(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public abstract void handleItem() throws IOException;

    protected abstract Task contentToTask(String content) throws JsonProcessingException;

    protected String taskToJSON(Long id) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Task task = tasks.stream().filter(t -> t.getId().equals(id)).findAny().orElseThrow(IllegalAccessError::new);
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    protected String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
