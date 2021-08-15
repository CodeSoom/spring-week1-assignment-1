package com.codesoom.assignment.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

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

    public synchronized Optional<Task> read(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public synchronized Collection<Task> readAll() {
        return tasks.values();
    }

    public synchronized Optional<Task> update(Long id, String title) {
        if (tasks.containsKey(id)) {
            Task task = new Task(id, title);
            tasks.replace(id, task);

            return Optional.of(task);
        }
        return Optional.empty();
    }

    public synchronized Optional<Task> delete(Long id) {
        return Optional.ofNullable(tasks.remove(id));
    }
}
