package com.codesoom.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    static private Long maxId = 1L;
    static private Map<Long, Task> tasks;

    TaskRepository() {
        tasks = new HashMap<>();
    }

    public Task taskBy(Long id) {
        return tasks.get(id);
    }

    public List<Task> tasksAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task save(Task task) {
        long newId = generateTaskId();
        task.setId(newId);
        tasks.put(newId, task);

        return task;
    }

    public void delete(Long id) {
        tasks.remove(id);
    }

    public Task update(Long oldTaskId, Task newTask) {
        newTask.setId(oldTaskId);
        tasks.replace(oldTaskId, newTask);
        return newTask;
    }

    private Long generateTaskId() {
        Long generatedId = maxId;
        maxId++;
        return generatedId;
    }
}
