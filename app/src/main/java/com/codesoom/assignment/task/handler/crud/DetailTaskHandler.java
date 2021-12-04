package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DetailTaskHandler extends CrudTaskHandler {

    public DetailTaskHandler(TaskService taskService) {
        super(taskService);
    }

    public void doHandle(HttpExchange httpExchange, Task task) throws IOException {
        new ResponseSuccess(httpExchange).send(toJson(task));
    }
}
