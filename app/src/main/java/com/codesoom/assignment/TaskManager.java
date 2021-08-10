package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();

    private Long lastTaskId = 0L;

    public Task findTaskFromId(long taskId) {
        return tasks.stream()
            .filter(task -> task.isMatchId(taskId))
            .findFirst()
            .get();
    }

    public Task toTask(String content) throws JsonProcessingException {
        Task task = new TaskMapper().getTaskFromContent(content);
        task.setId(++lastTaskId);

        return task;
    }

    public boolean existTaskFromId(long taskId) {
        return tasks.stream()
            .anyMatch(task -> task.isMatchId(taskId));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
