package com.codesoom.assignment;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {
    private final Map<Integer, Task> tasks;

    public TaskRepository() {
        this.tasks = new HashMap<>();
    }

    public Task findById(int id) {
        return this.tasks.get(id);
    }

    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }

    public Task deleteTask(int id) {
        return this.tasks.remove(id);
    }

    public Map<Integer, Task> findAll() {
        return this.tasks;
    }

    public Task updateTask(Task task) {
        return this.tasks.replace(task.getId(), task);
    }
}
