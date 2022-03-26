package com.codesoom.assignment.repository;

import com.codesoom.assignment.domain.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {
    private static final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task find(Long taskId) {
        return tasks.get(taskId);
    }
}
