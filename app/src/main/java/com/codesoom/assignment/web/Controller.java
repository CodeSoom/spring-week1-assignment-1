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

    HttpResponse getTasks() throws JsonProcessingException {
        String content = transfer.taskListToJson(taskApplicationService.getAllTasks());
        return new HttpResponse(200, content);
    }

    HttpResponse getTasksWithId(Long id) throws JsonProcessingException {
        Optional<Task> task = taskApplicationService.findTask(id);

        if (task.isEmpty()) {
            return new HttpResponse(404, "Not Found");
        }
        String content = transfer.taskToJson(task.orElseThrow());
        return new HttpResponse(200, content);
    }

    HttpResponse postTask(String body) throws JsonProcessingException {
        Task requestTask = transfer.jsonStringToTask(body);

        Long taskId = taskApplicationService.createTask(requestTask.getTitle());
        Task task = taskApplicationService.findTask(taskId).orElseThrow();

        String content = transfer.taskToJson(task);
        return new HttpResponse(201, content);
    }

    HttpResponse putTask(Long id, String body) throws JsonProcessingException {
        Task requestTask = transfer.jsonStringToTask(body);

        Optional<Task> result = taskApplicationService.updateTaskTitle(id, requestTask.getTitle())
                .flatMap(it -> taskApplicationService.findTask(id));
        if (result.isEmpty()) {
            return new HttpResponse(404, "Not Found");
        }

        String content = transfer.taskToJson(result.orElseThrow());
        return new HttpResponse(200, content);
    }

    HttpResponse deleteTask(Long id) {
        Optional<Object> result = taskApplicationService.deleteTask(id);

        if (result.isEmpty()) {
            return new HttpResponse(404, "Not Found");
        }
        return new HttpResponse(204, "Delete");
    }
}
