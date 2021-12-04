package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.response.ResponseBadRequest;
import com.codesoom.assignment.response.ResponseCreate;
import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class SaveTaskHandler extends CrudTaskHandler{

    public SaveTaskHandler(TaskService taskService) {
        super(taskService);
    }

    public void doHandle(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);

        if (!taskValidator.vaildBody(body)) {
            new ResponseBadRequest(httpExchange).send("title은 필수 값입니다.");
        }

        Task task = toTask(body);

        if (!taskValidator.vaildTaskTitle(task.getTitle())) {
            new ResponseBadRequest(httpExchange).send("title은 필수 값입니다.");
        }

        Task newTask = taskService.saveTask(task);

        new ResponseCreate(httpExchange).send(toJson(newTask));
    }
}
