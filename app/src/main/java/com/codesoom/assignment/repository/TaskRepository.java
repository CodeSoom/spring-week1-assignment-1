package com.codesoom.assignment.repository;

import com.codesoom.assignment.dto.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {
    private final Map<Long, Task> tasks;

    public TaskRepository() {
        this.tasks = new HashMap<>();
    }

    public Task findById(Long id) {
        return this.tasks.get(id);
    }

    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }

    public Task deleteTask(Long id) {
        return this.tasks.remove(id);
    }

    public Map<Long, Task> findAll() {
        return this.tasks;
    }

    public Task updateTask(Long id, String title) {
        Task task = this.tasks.get(id);
        if (task != null) {
            task.setTitle(title);
        }
        return this.tasks.get(id);
    }
}
