package com.codesoom.assignment.services;

import com.codesoom.assignment.IdGenerator;
import com.codesoom.assignment.modles.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TaskService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final InjectableValues.Std VALUE_INJECTOR = new InjectableValues.Std();

    private final Map<Long, Task> tasks;

    public TaskService() {
        tasks = new LinkedHashMap<>();
    }

    public Task createTask(final String content) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final InjectableValues.Std valueInjector = new InjectableValues.Std();
        final Task task = objectMapper.readValue(content, Task.class);
        tasks.put(task.getId(), task);
        return task;
    }

    public void setTask(final Task task) {
        tasks.put(task.getId(), task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> getTask(final Long id) {
        return Optional.ofNullable(tasks.get(id));
    }
}
