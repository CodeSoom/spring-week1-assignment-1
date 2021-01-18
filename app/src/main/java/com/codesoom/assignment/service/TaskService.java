package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private List<Task> tasks = new ArrayList<>();

    public Task getTask(long id) {
        return findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public Task createNewTask(String title) {
        Task task = new Task(generateTaskId(), title);
        tasks.add(task);
        System.out.println("Completed to create task - " + task.toString());
        return task;
    }

    public void updateTask(long id, String newTitle) {
        Task task = findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));
        task.setTitle(newTitle);
        System.out.println("Completed to update task - " + task.toString());
    }

    public void deleteTask(long id) {
        Task task = findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));
        tasks.remove(task);
        System.out.println("Completed to delete task - " + task.toString());
    }

    private Optional<Task> findTaskById(long id) {
        return tasks.stream().filter(e -> e.getId() == id).findFirst();
    }

    private long generateTaskId() {
        long id = 1;

        if (!tasks.isEmpty()) {
            id = tasks.stream().mapToLong(Task::getId).max().getAsLong() + 1;
        }

        return id;
    }
}
