package com.codesoom.assignment;

import com.codesoom.assignment.errors.TaskIdNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static final TaskManager uniqueInstance = new TaskManager();

    private final TaskFactory taskFactory = new TaskFactory();
    private final List<Task> tasks = new ArrayList<>();

    private Long lastId = 0L;

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return uniqueInstance;
    }

    public Task findTaskWith(long id) {
        return tasks.stream()
            .filter(task -> task.isMatchId(id))
            .findFirst()
            .orElseThrow(TaskIdNotFoundException::new);
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task createTask(String title) throws JsonProcessingException {
        Task task = taskFactory.toTask(title);

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

    public Task updateTask(long id, String body) throws JsonProcessingException {
        Task task = findTaskWith(id);
        Task content = taskFactory.toTask(body);

        String newTitle = content.getTitle();
        task.setTitle(newTitle);

        return task;
    }
}
