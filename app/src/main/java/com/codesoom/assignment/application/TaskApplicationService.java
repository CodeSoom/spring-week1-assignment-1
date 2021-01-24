package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;

import java.util.*;

public class TaskApplicationService {
    private final Map<Long, Task> taskMap = new HashMap<>();
    private Long lastId = 1L;

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public Long createTask(String title) {
        Long id = getNextId();
        Task newTask = new Task(id, title);
        taskMap.put(id, newTask);
        return id;
    }

    public Optional<Task> findTask(Long taskId) {
        if (!taskMap.containsKey(taskId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(taskMap.get(taskId));
    }

    private Long getNextId() {
        return lastId++;
    }

    public Optional<Object> updateTaskTitle(Long taskId, String newTitle) {
        Optional<Task> targetTask = findTask(taskId).map(
                it -> it.updateTaskTitle(newTitle)
        );
        return putTaskToMap(targetTask);
    }

    private Optional<Object> putTaskToMap(Optional<Task> task) {
        if (task.isEmpty()) {
            return Optional.empty();
        }
        Task targetTask = task.get();
        taskMap.put(targetTask.getId(), targetTask);
        return Optional.of(new Object());
    }

    public Optional<Object> deleteTask(Long taskId) {
        return deleteTask(findTask(taskId));
    }

    private Optional<Object> deleteTask(Optional<Task> task) {
        if (task.isEmpty()) {
            return Optional.empty();
        }
        Task targetTask = task.get();
        taskMap.remove(targetTask.getId());
        return Optional.of(new Object());
    }
}
