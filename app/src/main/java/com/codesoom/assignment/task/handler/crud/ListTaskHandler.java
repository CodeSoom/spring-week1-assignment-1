package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.task.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class ListTaskHandler extends CrudTaskHandler{

    public ListTaskHandler(TaskService taskService) {
        super(taskService);
    }

    public void doHandle(HttpExchange httpExchange) throws IOException {
        new ResponseSuccess(httpExchange).send(toJson(taskService.findALL()));
    }
}
