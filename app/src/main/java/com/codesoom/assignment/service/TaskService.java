package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskId;
import com.codesoom.assignment.utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {
    final List<Task> tasks;

    public TaskService() {
         tasks = new ArrayList<>();
    }

    public String getTasks() throws IOException {
        String data = JsonUtil.writeValue(tasks);

        return data;
    }

    public String getTaskByUserId(Long userId) throws IOException {
        String data = "";
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getId().equals(userId))
                .findFirst();

        if (task.isPresent()) {
            data = JsonUtil.writeValue(task.get());
        }
        return data;
    }

    public String createTask(String requestBody) throws IOException {
        Task task = JsonUtil.readValue(requestBody, Task.class);
        task.setId(TaskId.getNewId());
        tasks.add(task);
        return JsonUtil.writeValue(task);
    }

    public String updateTask(Long userId, String requestBody) throws IOException {
        String data = "";
        String newTitle = JsonUtil.readValue(requestBody, Task.class).getTitle();
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getId().equals(userId))
                .findFirst();

        if (task.isPresent()) {
            int indexOfOriginTask = tasks.indexOf(task.get());
            Task originTask = tasks.get(indexOfOriginTask);
            originTask.setTitle(newTitle);
            data = JsonUtil.writeValue(originTask);
        }

        return data;
    }

    public void deleteTask(Long userId) {
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getId().equals(userId))
                .findFirst();

        task.ifPresent(value -> tasks.remove(value));
    }
}
