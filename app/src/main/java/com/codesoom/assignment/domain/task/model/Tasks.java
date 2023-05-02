package com.codesoom.assignment.domain.task.model;

import java.util.ArrayList;
import java.util.List;

public class Tasks {

    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found";

    private final List<Task> tasks;
    private long currentId;

    public Tasks() {
        this.tasks = new ArrayList<>();
        this.currentId = 1;
    }

    public void add(final Task task) {
        task.setId(currentId++);
        tasks.add(task);
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    public Task findById(final long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(TASK_NOT_FOUND_MESSAGE));
    }

    public Task update(final long id, final String title) {
        Task task = findById(id);
        task.setTitle(title);
        return task;
    }

    public void delete(final long id) {
        Task task = findById(id);
        tasks.remove(task);
    }

}
