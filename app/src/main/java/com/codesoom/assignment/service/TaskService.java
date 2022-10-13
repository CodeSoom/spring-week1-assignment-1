package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskId;
import com.codesoom.assignment.utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private static final TaskService taskService = new TaskService();
    private static final List<Task> tasks = new ArrayList<>();

    private TaskService() {
    }

    public static TaskService getInstance() {
        return taskService;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Optional<Task> getTaskByUserId(Long userId) {
        return tasks.stream()
                .filter(t -> t.getId().equals(userId))
                .findFirst();
    }

    public Task createTask(String requestBody) throws IOException {
        Task task = JsonUtil.readValue(requestBody, Task.class);
        task.setId(TaskId.getNewId());
        tasks.add(task);

        return task;
    }

    public Task updateTask(Task originTask, String requestBody) throws IOException {
        String newTitle = JsonUtil.readValue(requestBody, Task.class).getTitle();
        int indexOfOriginTask = tasks.indexOf(originTask);
        Task task = tasks.get(indexOfOriginTask);
        task.setTitle(newTitle);

        return task;
    }

    public boolean deleteTask(Task task) {
        return tasks.remove(task);
    }
}
