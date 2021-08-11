package com.codesoom.assignment;

import com.codesoom.assignment.errors.TaskIdNotFoundException;
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

    public Task findTaskWith(long id) {
        return tasks.stream()
            .filter(task -> task.isMatchId(id))
            .findFirst()
            .orElseThrow(TaskIdNotFoundException::new);
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

    public Task deleteTask(long id) {
        Task task = findTaskWith(id);
        tasks.remove(task);

        return task;
    }

    public Task updateTask(long id, Task content) {
        Task task = findTaskWith(id);

        task.setTitle(content.getTitle());

        return task;
    }
}
