package com.codesoom.assignment.repository;

import com.codesoom.assignment.domain.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {
    private static final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    public void remove(long taskId) { tasks.remove(taskId); }

    public Task find(long taskId) { return tasks.get(taskId); }

    public Task update(long taskId, String title) {
        tasks.get(taskId).setTitle(title);
        return tasks.get(taskId);
    }

    public Collection<Task> findAll() {
        return tasks.values();
    }

    public void removeAll() {
        tasks.clear();
    }
}
