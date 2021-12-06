package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.response.ResponseNoContent;
import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteTaskHandler extends CrudTaskHandler{

    public DeleteTaskHandler(TaskService taskService) {
        super(taskService);
    }

    public void doHandle(HttpExchange httpExchange, Task task) throws IOException {
        taskService.removeTask(task);

        new ResponseNoContent(httpExchange).send("");
    }
}
