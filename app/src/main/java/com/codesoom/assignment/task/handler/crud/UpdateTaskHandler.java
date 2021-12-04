package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.response.ResponseBadRequest;
import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class UpdateTaskHandler extends CrudTaskHandler{

    public UpdateTaskHandler(TaskService taskService) {
        super(taskService);
    }

    public void doHandle(HttpExchange httpExchange, Task task) throws IOException {
        String body = getBody(httpExchange);

        if (!taskValidator.vaildBody(body)) {
            new ResponseBadRequest(httpExchange).send("body 값은 필수 값입니다.");
        }

        Task source = toTask(body);

        if (!taskValidator.vaildTaskTitle(source.getTitle())) {
            new ResponseBadRequest(httpExchange).send("title은 필수 값입니다.");
        }

        taskService.updateTask(task, source);

        new ResponseSuccess(httpExchange).send(toJson(task));
    }
}
