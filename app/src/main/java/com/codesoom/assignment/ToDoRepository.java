package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ToDoRepository {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

    public Optional<Task> getTaskById(Long taskId) {
        return tasks
                .stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void updateTask(Task existedTask, String content) throws JsonProcessingException {
        Task newTask = objectMapper.readValue(content, Task.class);
        existedTask.setTitle(newTask.getTitle());
    }

    public void deleteTask(Task targetTask) {
        tasks.removeIf(task -> task.getId().equals(targetTask.getId()));
    }

    public List<Task> getTasks() {
        return tasks;
    }

}