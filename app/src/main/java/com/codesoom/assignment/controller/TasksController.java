package com.codesoom.assignment.controller;

import com.codesoom.assignment.RequestMapping;
import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.RequestMethod;
import com.codesoom.assignment.service.TaskService;

import java.io.IOException;

public class TasksController {

    private TaskService taskService = new TaskService();

    @RequestMapping(method = RequestMethod.HEAD, path = "/")
    public HttpResponse root(HttpRequest httpRequest) {
        return new HttpResponse(HttpStatus.OK, "");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    public HttpResponse getTasks(HttpRequest httpRequest) throws IOException {
        return new HttpResponse(HttpStatus.OK, taskService.getTasks());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{id}")
    public HttpResponse getTask(HttpRequest httpRequest) throws IOException {
        Long id = getIdFromPath(httpRequest.getPath());
        String content = taskService.getTask(id);

        if (content.isEmpty()) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }

        return new HttpResponse(HttpStatus.OK, content);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/tasks")
    public HttpResponse addTask(HttpRequest httpRequest) throws IOException {
        return new HttpResponse(HttpStatus.CREATED, taskService.addTask(httpRequest.getBody()));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/tasks/{id}")
    public HttpResponse updateTask(HttpRequest httpRequest) throws IOException {
        Long id = getIdFromPath(httpRequest.getPath());

        if (taskService.getTask(id).isEmpty()) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }

        String content = taskService.updateTask(id, httpRequest.getBody());
        return new HttpResponse(HttpStatus.OK, content);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/tasks/{id}")
    public HttpResponse deleteTask(HttpRequest httpRequest) throws IOException {
        Long id = getIdFromPath(httpRequest.getPath());

        if (taskService.getTask(id).isEmpty()) {
            return new HttpResponse(HttpStatus.NOT_FOUND);
        }

        taskService.deleteTask(id);
        return new HttpResponse(HttpStatus.NO_CONTENT);
    }


    private Long getIdFromPath(String path) {
        Long id = Long.valueOf(path.replace("/tasks/", ""));
        return id;
    }

}
