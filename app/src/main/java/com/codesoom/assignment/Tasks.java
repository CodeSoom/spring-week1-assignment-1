package com.codesoom.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tasks {
    private List<Task> tasks;

    public Tasks() {
        this.tasks = new ArrayList<>();
    }

    public Tasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void remove(Task task) {
        this.tasks.remove(task);
    }

    public Optional<Task> findTask(Long id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public Task findByPath(String path) {
        Long pathVariable = extractPathVariable(path);
        return this.findTask(pathVariable).orElse(null);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    private Long extractPathVariable(String path) {
        return Long.parseUnsignedLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
