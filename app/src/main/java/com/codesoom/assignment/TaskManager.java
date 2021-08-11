package com.codesoom.assignment;

import com.codesoom.assignment.errors.TaskIdNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static final TaskManager uniqueInstance = new TaskManager();

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return uniqueInstance;
    }

    private final TaskConvertor taskConvertor = new TaskConvertor();
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

    public Task createTask(String body) throws JsonProcessingException {
        Task task = taskConvertor.toTask(body);

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
        Task content = taskConvertor.toTask(body);

        String newTitle = content.getTitle();
        task.setTitle(newTitle);

        return task;
    }
}
