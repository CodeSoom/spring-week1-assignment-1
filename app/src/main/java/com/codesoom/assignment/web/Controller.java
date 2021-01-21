package com.codesoom.assignment.web;

import com.codesoom.assignment.application.TaskApplicationService;
import com.codesoom.assignment.application.TaskJsonTransfer;
import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public class Controller {
    TaskApplicationService taskApplicationService;
    TaskJsonTransfer transfer;

    public Controller(TaskApplicationService taskApplicationService) {
        this.transfer = new TaskJsonTransfer();
        this.taskApplicationService = taskApplicationService;
    }

    HttpResponse getTasks(HttpRequest request) throws JsonProcessingException {
        String content = transfer.taskListToJson(taskApplicationService.getAllTasks());
        return new HttpResponse(200, content);
    }

    HttpResponse getTasksWithId(HttpRequest request) throws JsonProcessingException {
        Optional<Task> task = taskApplicationService.findTask(request.getTaskId());

        if (task.isEmpty()) {
            return new HttpResponse(404, "Not Found");
        }
        String content = transfer.taskToJson(task.orElseThrow());
        return new HttpResponse(200, content);
    }

    HttpResponse postTask(HttpRequest request) throws JsonProcessingException {
        Task requestTask = transfer.jsonStringToTask(request.requestBody);

        Long taskId = taskApplicationService.createTask(requestTask.getTitle());
        Task task = taskApplicationService.findTask(taskId).orElseThrow();

        String content = transfer.taskToJson(task);
        return new HttpResponse(201, content);
    }

    HttpResponse putTask(HttpRequest request) throws JsonProcessingException {
        Long taskId = request.getTaskId();
        Task requestTask = transfer.jsonStringToTask(request.requestBody);

        Optional<Task> result = taskApplicationService.updateTaskTitle(taskId, requestTask.getTitle())
                .flatMap(it -> taskApplicationService.findTask(taskId));
        if (result.isEmpty()) {
            return new HttpResponse(404, "Not Found");
        }

        String content = transfer.taskToJson(result.orElseThrow());
        return new HttpResponse(200, content);
    }

    HttpResponse deleteTask(HttpRequest request) {
        Optional<Object> result = taskApplicationService.deleteTask(request.getTaskId());

        if (result.isEmpty()) {
            return new HttpResponse(404, "Not Found");
        }
        return new HttpResponse(204, "Delete");
    }
}
