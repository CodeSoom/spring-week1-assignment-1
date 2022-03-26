package com.codesoom.assignment.repository;

import com.codesoom.assignment.domain.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {
    private static long taskSequence = 0;
    private final Map<Long, Task> tasks = new HashMap<>();

    public void save(String title) {
        Task task = new Task(++taskSequence, title);
        tasks.put(task.getId(), task);
    }

    public Task find(Long taskId) {
        return tasks.get(taskId);
    }
}
