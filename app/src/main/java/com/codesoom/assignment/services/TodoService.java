package com.codesoom.assignment.services;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.codesoom.assignment.utils.JsonParser;

import java.io.IOException;

public class TodoService {

    private final TaskList taskList;

    public TodoService() {
        taskList = TaskList.getTaskList();
    }

    public String getTodos() throws IOException {
        return JsonParser.toJsonString(taskList.getTasks());
    }

    public String addTodo(Task newTask) {
        taskList.addTask(newTask);

        return HttpStatusCode.CREATED.getMessage();
    }

    public String getTodo(Long taskId) throws IOException {
        Task selected = taskList.getTask(taskId);
        return selected != null
                ? JsonParser.toJsonString(selected)
                : HttpStatusCode.NOT_FOUND.getMessage();
    }

    public String editTask(Long taskId, Task task) {
        Task selected = taskList.getTask(taskId);

        if (selected == null) {
            return HttpStatusCode.NOT_FOUND.getMessage();
        }

        selected.setTitle(task.getTitle());

        return HttpStatusCode.OK.getMessage();
    }

    public String deleteTask(Long taskId) {
        Task selected = taskList.getTask(taskId);

        if (selected == null) {
            return HttpStatusCode.NOT_FOUND.getMessage();
        }

        taskList.deleteTask(taskId);

        return HttpStatusCode.OK.getMessage();
    }
}
