package com.codesoom.assignment.services;

import com.codesoom.assignment.modles.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class TaskService {
    private final Map<Long, Task> tasks;

    public TaskService() {
        tasks = new HashMap<>();
    }

    public void setTask(final Task task) {
        tasks.put(task.getId(), task);
    }

    public Map<Long, Task> getTasks() {
        return tasks;
    }

    public Optional<Task> getTask(final Long id) {
        return Optional.ofNullable(tasks.get(id));
    }
}
