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
    private final Map<Long, Task> tasks;
    private final IdGenerator idGenerator;

    public TaskService() {
        tasks = new LinkedHashMap<>();
        idGenerator = new IdGenerator();
    }

    public synchronized Task createTask(final String content) throws JsonProcessingException {
        final InjectableValues.Std valueInjector = new InjectableValues.Std();
        valueInjector.addValue(Long.class, idGenerator.generateId());
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setInjectableValues(valueInjector);
        final Task task = objectMapper.readValue(content, Task.class);
        tasks.put(task.getId(), task);
        return task;

    }

    public synchronized void setTask(final Task task) {
        tasks.put(task.getId(), task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> getTask(final Long id) {
        return Optional.ofNullable(tasks.get(id));
    }
}
