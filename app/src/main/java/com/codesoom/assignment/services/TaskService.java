package com.codesoom.assignment.services;

import com.codesoom.assignment.modles.Task;

import java.util.*;

public final class TaskService {
    private final Map<Long, Task> tasks;

    public TaskService() {
        tasks = new LinkedHashMap<>();
    }

    public void setTask(final Task task) {
        tasks.put(task.getId(), task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> getTask(final Long id) {
        return Optional.ofNullable(tasks.get(id));
    }
}
