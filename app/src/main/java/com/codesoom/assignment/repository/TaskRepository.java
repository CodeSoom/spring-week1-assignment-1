package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private Long sequence = 0L;
    private Map<Long, Task> taskMap = new HashMap<>();

    public Task findById(Long id) {
        return taskMap.get(id);
    }

    public List<Task> findAll() {
        return new ArrayList<>(taskMap.values());
    }

    public Long save(Task task) {
        if (task.getId() == null) {
            task.setId(++sequence);
        }

        taskMap.put(task.getId(), task);
        return task.getId();
    }

    public void delete(Long id) {
        taskMap.remove(id);
    }

    public Long update(Long id, Task task) {
        Task findTask = taskMap.get(id);
        findTask.setTitle(task.getTitle());
        return id;
    }

}
