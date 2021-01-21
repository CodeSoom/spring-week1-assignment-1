package com.codesoom.assignment.web;

import com.codesoom.assignment.application.TaskApplicationService;
import com.codesoom.assignment.application.TaskJsonTransfer;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public class Controller {
    TaskApplicationService taskApplicationService;
    TaskJsonTransfer transfer;

    public Controller(TaskApplicationService taskApplicationService) {
        this.transfer = new TaskJsonTransfer();
        this.taskApplicationService = taskApplicationService;
    }

    Optional<HttpResponse> getTasks(HttpRequest request) {
        return transfer.taskListToJson(taskApplicationService.getAllTasks()).map(
                it -> new HttpResponse(200, it)
        );
    }

    Optional<HttpResponse> getTasksWithId(HttpRequest request) {
        return taskApplicationService.findTask(request.getTaskId()).flatMap(
                it -> transfer.taskToJson(it)
        ).map(
                it -> new HttpResponse(200, it)
        ).or(
                () -> Optional.of(new HttpResponse(404, "Not Found"))
        );
    }

    Optional<HttpResponse> postTask(HttpRequest request) {
        return transfer.jsonStringToTask(request.requestBody).map(
                it -> taskApplicationService.createTask(it.getTitle())
        ).flatMap(
                it -> taskApplicationService.findTask(it)
        ).flatMap(
                it -> transfer.taskToJson(it)
        ).map(
                it -> new HttpResponse(201, it)
        ).or(
                () -> Optional.of(new HttpResponse(404, "Not Found"))
        );
    }

    Optional<HttpResponse> putTask(HttpRequest request) {
        Long taskId = request.getTaskId();
        return transfer.jsonStringToTask(request.requestBody).map(
                it -> taskApplicationService.updateTaskTitle(taskId, it.getTitle())
        ).flatMap(
                it -> taskApplicationService.findTask(taskId)
        ).flatMap(
                it -> transfer.taskToJson(it)
        ).map(
                it -> new HttpResponse(200, it)
        ).or(
                () -> Optional.of(new HttpResponse(404, "Not Found"))
        );
    }

    Optional<HttpResponse> deleteTask(HttpRequest request) {
        return taskApplicationService.deleteTask(request.getTaskId()).map(
               it -> new HttpResponse(204, "Delete")
        ).or(
                () -> Optional.of(new HttpResponse(404, "Not Found"))
        );
    }
}
