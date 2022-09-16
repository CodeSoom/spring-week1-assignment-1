package com.codesoom.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private final static Map<Long, Task> tasks = new HashMap<>();
    private static Long id = 1L;

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task save(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Task update(Task task) {
        Task findOne = findById(task.getId());
        findOne.setTitle(task.getTitle());
        return findOne;
    }

    public void delete(Long id) {
        tasks.remove(id);
    }

    public Task findById(Long id) {
        return tasks.get(id);
    }

    public boolean isExist(Long id) {
        return findById(id) != null;
    }
}
