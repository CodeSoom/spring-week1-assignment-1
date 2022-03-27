package com.codesoom.assignment.services;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.codesoom.assignment.networks.BaseResponse;

import java.util.List;

public class TodoService {

    private final TaskList taskList;

    public TodoService() {
        taskList = TaskList.getTaskList();
    }

    public List<Task> getTodos() {
        return taskList.getTasks();
    }

    public Task addTodo(Task newTask) {
        return taskList.addTask(newTask);
    }

    public Task getTodo(Long taskId) {
        return taskList.getTask(taskId);
    }

    public BaseResponse<Task> editTask(Long taskId, Task task) {
        Task selected = taskList.getTask(taskId);

        if (selected == null) {
            return new BaseResponse<>(HttpStatusCode.NOT_FOUND);
        }

        return new BaseResponse<>(HttpStatusCode.OK, selected.editTaskTitle(task.getTitle()));
    }

    public HttpStatusCode deleteTask(Long taskId) {
        Task selected = taskList.getTask(taskId);

        if (selected == null) {
            return HttpStatusCode.NOT_FOUND;
        }

        taskList.deleteTask(taskId);

        return HttpStatusCode.NO_CONTENT;
    }
}
