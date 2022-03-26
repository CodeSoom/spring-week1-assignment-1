package com.codesoom.assignment.controller;

import com.codesoom.assignment.HttpResponse;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TodoService;
import com.codesoom.assignment.utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TodoController {

    private TodoService todoService = new TodoService();

    public HttpResponse findAllTasks() {
        List<Task> taskList = todoService.findAllTasks();

        try {
            String response = JsonUtils.tasksToJson(taskList);
            return new HttpResponse(200, response);
        } catch (IOException e) {
            return new HttpResponse(500, "서버에러가 발생했습니다.");
        }

    }

    public HttpResponse findTaskById(Long taskId) {
        Optional<Task> task = todoService.findTaskById(taskId);
        if(task.isEmpty()) {
            return new HttpResponse(404, "존재하지 않는 Task입니다!");
        }

        try {
            String response = JsonUtils.taskToJson(task.get());
            return new HttpResponse(200, response);
        } catch (IOException e) {
            return new HttpResponse(500, "서버에러가 발생했습니다.");
        }

    }

    public HttpResponse createTask(String requestBody) {
        if(requestBody.isBlank()) {
            return new HttpResponse(400, "입력값이 존재하지 않습니다");
        }

        try {
            Task requestTaskInfo = JsonUtils.stringToTask(requestBody);
            Task task = todoService.saveTask(requestTaskInfo);
            String response = JsonUtils.taskToJson(task);
            return new HttpResponse(201, response);

        } catch (IOException e) {
            return new HttpResponse(500, "서버에러가 발생했습니다.");
        }

    }

    public HttpResponse updateTask(Long taskId, String requestBody) {
        if(requestBody.isBlank()) {
            return new HttpResponse(400, "입력값이 존재하지 않습니다");
        }

        Optional<Task> task = todoService.findTaskById(taskId);
        if(task.isEmpty()) return new HttpResponse(404, "존재하지 않는 Task입니다!");

        try {
            Task requestTaskInfo = JsonUtils.stringToTask(requestBody);
            Task updatedTask = todoService.updateTask(task.get(), requestTaskInfo);
            String response = JsonUtils.taskToJson(updatedTask);
            return new HttpResponse(200, response);
        } catch (IOException e) {
            return new HttpResponse(500, "서버에러가 발생했습니다.");
        }
    }

    public HttpResponse deleteTaskById(Long taskId) {
        String response = "Task가 성공적으로 삭제되었습니다";

        Optional<Task> task = todoService.findTaskById(taskId);
        if(task.isEmpty()) {
            return new HttpResponse(404, "존재하지 않는 Task입니다!");
        }

        todoService.deleteTask(task.get());

        return new HttpResponse(204, response);
    }
}
