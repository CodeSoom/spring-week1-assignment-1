package com.codesoom.assignment.handler.impl;

import com.codesoom.assignment.handler.TaskHandler;
import com.codesoom.assignment.model.RequestBody;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.vo.HttpMethod;
import com.codesoom.assignment.vo.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;

import static com.codesoom.assignment.utils.ParseUtil.parseTaskToJsonString;

public class CreateTaskHandler extends TaskHandler {

    public CreateTaskHandler(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    public void handle() throws IOException {
        RequestBody requestBody = new RequestBody(exchange);
        Task createTask = requestBody.read(Task.class);
        createTask(createTask);
        String createdTaskJson = parseTaskToJsonString(createTask);
        new ResponseSuccess(exchange).send(createdTaskJson, HttpStatus.CREATED.getCode());
    }

    /**
     * 할일을 생성한다.
     */
    private void createTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public boolean isRequest() {
        URI uri = exchange.getRequestURI();
        return HttpMethod.POST.name().equals(method) && path.equals(uri.getPath());
    }
}
