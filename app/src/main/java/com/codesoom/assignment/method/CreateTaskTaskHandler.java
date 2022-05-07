package com.codesoom.assignment.method;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CreateTaskTaskHandler extends TaskHandler {

    public CreateTaskTaskHandler(HttpExchange exchange, Task task) {
        super(exchange);
    }

    public void handleItem() throws JsonProcessingException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
                .collect(Collectors.joining("\n"));
        if (!body.isEmpty()) {
            Task task = contentToTask(body, true);
            tasks.add(task);
        }
        String content = tasksToJSON();
        new ResponseCreated(exchange).send(content);
    }

    private Task contentToTask(String content, boolean isPostMethod) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(new Long(id));
        if (isPostMethod) {
            id += 1;
        }
        return task;
    }
}
