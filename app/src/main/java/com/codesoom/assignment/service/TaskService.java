package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;

import java.util.*;

public class TaskService {
    private Map<Long, Task> tasks = new HashMap<>();

    public Task getTask(long id) {
        return findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task createNewTask(String title) {
        Task task = new Task(generateNewTaskId(), title);
        tasks.put(task.getId(), task);
        System.out.println("Completed to create task - " + task.toString());
        return task;
    }

    public Task updateTask(long id, String newTitle) {
        Task task = findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));;
        task.setTitle(newTitle);
        System.out.println("Completed to update task - " + task.toString());
        return task;
    }

    public void deleteTask(long id) {
        Task task = findTaskById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find task (ID: " + id + ")"));;
        tasks.remove(task.getId());
        System.out.println("Completed to delete task - " + task.toString());
    }

    private Optional<Task> findTaskById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    private long generateNewTaskId() {
        long id = 1;

        if (!tasks.isEmpty()) {
            //Task 삭제를 대비하여 가장 최근 task의 ID를 기준으로 생성
            id = tasks.keySet().stream().mapToLong(Long::longValue).max().getAsLong() + 1;
        }
        return id;
    }
}
