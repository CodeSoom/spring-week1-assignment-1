package com.codesoom.assignment.models;

import java.util.Collection;
import java.util.HashMap;

public class TasksStorage {
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private Long newId;

    public TasksStorage() {
        newId = 0L;
    }

    public synchronized Task create(String title) {
        Task task = new Task(newId, title);
        tasks.put(task.getId(), task);

        newId++;

        return task;
    }

    public synchronized Task read(Long id) {
        return tasks.get(id);
    }

    public synchronized Collection<Task> readAll() {
        return tasks.values();
    }

    public synchronized Task update(Long id, String title) {
        Task task = tasks.get(id);

        if(task == null) {
            return null;
        }

        task.setTitle(title);
        return task;
    }

    public synchronized Task delete(Long id) {
        return tasks.remove(id);
    }
}
