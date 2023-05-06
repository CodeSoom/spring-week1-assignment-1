package com.codesoom.assignment.task.repository;

import com.codesoom.assignment.task.exception.TaskNotFoundException;
import com.codesoom.assignment.task.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Tasks {

    private final List<Task> tasks;
    private long currentId;

    public Tasks() {
        this.tasks = new ArrayList<>();
    }

    public void add(final Task task) {
        task.setId(increaseId());
        tasks.add(task);
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    public Task findById(final long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
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

    private long increaseId() {
        return currentId += 1;
    }

}
