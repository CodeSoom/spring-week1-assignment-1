package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static final TaskManager uniqueInstance = new TaskManager();

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return uniqueInstance;
    }

    private final List<Task> tasks = new ArrayList<>();

    private Long lastId = 0L;

    public Task findTaskFromId(long taskId) {
        return tasks.stream()
            .filter(task -> task.isMatchId(taskId))
            .findFirst()
            .get();
    }

    public boolean existTaskFromId(long taskId) {
        return tasks.stream()
            .anyMatch(task -> task.isMatchId(taskId));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task createTask(Task task) {
        lastId++;
        task.setId(lastId);
        tasks.add(task);

        return task;
    }

    public Task deleteTask(long taskId) {
        Task task = findTaskFromId(taskId);
        tasks.remove(task);

        return task;
    }

    public Task updateTask(long taskId, Task content) {
        Task task = findTaskFromId(taskId);

        task.setTitle(content.getTitle());

        return task;
    }
}
