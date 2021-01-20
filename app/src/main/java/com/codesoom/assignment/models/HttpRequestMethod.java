package com.codesoom.assignment.models;

import com.codesoom.assignment.service.TaskService;

import java.util.function.BiFunction;

import static com.codesoom.assignment.models.HttpRequestForTasks.*;

public enum HttpRequestMethod {
    GET((httpRequest, taskService) -> {
        String path = httpRequest.getPath();
        if (path.equals(TASKS)) {
            return new HttpResponse(HttpStatus.OK, taskService.getTasks());
        }

        Long id = getIdFromPath(path);
        String content = taskService.getTask(id);

        if (content.isEmpty()) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }

        return new HttpResponse(HttpStatus.OK, content);
    }),
    POST((httpRequest, taskService) -> {
        return new HttpResponse(HttpStatus.CREATED, taskService.addTask(httpRequest.getBody()));
    }),
    PATCH((httpRequest, taskService) -> {
        Long id = getIdFromPath(httpRequest.getPath());

        if (taskService.getTask(id).isEmpty()) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }

        String content = taskService.updateTask(id, httpRequest.getBody());
        return new HttpResponse(HttpStatus.OK, content);
    }),
    PUT(HttpRequestMethod.PATCH.expression),
    DELETE((httpRequest, taskService) -> {
        Long id = getIdFromPath(httpRequest.getPath());

        if (taskService.getTask(id).isEmpty()) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }

        taskService.deleteTask(id);
        return new HttpResponse(HttpStatus.NO_CONTENT);
    });

    private BiFunction<HttpRequest, TaskService, HttpResponse> expression;

    HttpRequestMethod(BiFunction<HttpRequest, TaskService, HttpResponse> expression) {
        this.expression = expression;
    }

    public HttpResponse createResponse(HttpRequest httpRequest, TaskService taskService) {
        return expression.apply(httpRequest, taskService);
    }

    private static Long getIdFromPath(String path) {
        Long id = Long.valueOf(path.replace(TASKS + "/", ""));
        return id;
    }

}
