package com.codesoom.assignment.controllers;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.networks.BaseResponse;
import com.codesoom.assignment.services.TodoService;

import java.util.List;

public class TodoController {

    private final TodoService todoService;

    public TodoController() {
        this.todoService = new TodoService();
    }

    public BaseResponse<List<Task>> getTodos() {
        return new BaseResponse<>(HttpStatusCode.OK, todoService.getTodos());
    }

    public BaseResponse<Task> postTodo(Task newTask) {
        return new BaseResponse<>(HttpStatusCode.CREATED, todoService.addTodo(newTask));
    }

    public BaseResponse<Task> getTodo(Long taskId) {
        if (!isValidTaskId(taskId)) {
            return new BaseResponse<>(HttpStatusCode.NOT_FOUND);
        }

        Task selectedTask = todoService.getTodo(taskId);

        if (selectedTask == null) {
            return new BaseResponse<>(HttpStatusCode.NOT_FOUND);
        }

        return new BaseResponse<>(HttpStatusCode.OK, selectedTask);
    }

    public BaseResponse editTask(Long taskId, Task task) {
        if (!isValidTaskId(taskId)) {
            return new BaseResponse<>(HttpStatusCode.NOT_FOUND);
        }

        if (!task.hasValidContent()) {
            return new BaseResponse<>(HttpStatusCode.BAD_REQUEST);
        }

        return todoService.editTask(taskId, task);
    }

    public BaseResponse deleteTodo(Long taskId) {
        if (!isValidTaskId(taskId)) {
            return new BaseResponse<>(HttpStatusCode.NOT_FOUND);
        }

        return new BaseResponse<>(todoService.deleteTask(taskId));
    }

    private boolean isValidTaskId(Long taskId) {
        return taskId > 0;
    }

}
