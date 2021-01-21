package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;

import java.util.*;

public class TaskService {
    private Map<Long, Task> tasks = new HashMap<>();
    private IdGenerator idGenerator = new IdGenerator();

    public Task getTask(long id) {
        return getTaskByIdOrThrow(id);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task createNewTask(String title) {
        Task task = new Task(idGenerator.generateNewTaskId(), title);
        tasks.put(task.getId(), task);
        System.out.println("Completed to create task - " + task.toString());
        return task;
    }

    public Task updateTask(long id, String newTitle) {
        Task task = getTaskByIdOrThrow(id);
        task.setTitle(newTitle);
        System.out.println("Completed to update task - " + task.toString());
        return task;
    }

    public void deleteTask(long id) {
        Task task = getTaskByIdOrThrow(id);
        tasks.remove(task.getId());
        System.out.println("Completed to delete task - " + task.toString());
    }

    private Task getTaskByIdOrThrow(long id) {
        return findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));
    }

    private Optional<Task> findTaskById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

}
