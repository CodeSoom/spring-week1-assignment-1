package com.codesoom.assignment.controllers;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TodoService;

import java.io.IOException;

public class TodoController {

    private final TodoService todoService;

    public TodoController() {
        this.todoService = new TodoService();
    }

    public String getTodos() throws IOException {
        return todoService.getTodos();
    }

    public String postTodo(Task newTask) {
        return todoService.addTodo(newTask);
    }

    public String getTodo(Long taskId) throws IOException {
        if (!isValidTaskId(taskId)) {
            return HttpStatusCode.WRONG_TASK_ID.getMessage();
        }

        return todoService.getTodo(taskId);
    }

    public String editTask(Long taskId, Task task) {
        if (!isValidTaskId(taskId)) {
            return HttpStatusCode.WRONG_TASK_ID.getMessage();
        }

        if (!task.hasValidContent()) {
            return HttpStatusCode.BAD_CONTENT_FORMAT.getMessage();
        }

        return todoService.editTask(taskId, task);
    }

    public String deleteTodo(Long taskId) {
        if (!isValidTaskId(taskId)) {
            return HttpStatusCode.WRONG_TASK_ID.getMessage();
        }

        return todoService.deleteTask(taskId);
    }

    private boolean isValidTaskId(Long taskId) {
        return taskId > 0;
    }

}
