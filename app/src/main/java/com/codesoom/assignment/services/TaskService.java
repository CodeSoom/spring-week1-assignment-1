package com.codesoom.assignment.services;

import com.codesoom.assignment.modles.Task;
import com.codesoom.assignment.utils.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TaskService {
    private static final String CAN_NOT_FIND_TASK_EXCEPTION = "Can not find task.";

    private final Map<Long, Task> tasks;
    private final IdGenerator idGenerator;

    public TaskService() {
        tasks = new LinkedHashMap<>();
        idGenerator = new IdGenerator();
    }

    private Task generateTask(final Long id, final String content) throws JsonProcessingException {
        final InjectableValues.Std valueInjector = new InjectableValues.Std();
        valueInjector.addValue(Long.class, id);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setInjectableValues(valueInjector);
        return objectMapper.readValue(content, Task.class);
    }

    public synchronized Task createTask(final String content) throws JsonProcessingException {
        final Task task = generateTask(idGenerator.generateId(), content);
        tasks.put(task.getId(), task);
        return task;
    }

    public synchronized Task updateTask(final Long id, final String content) throws Exception {
        final Task task = getTask(id);
        final Task updatedTask = generateTask(id, content);
        tasks.put(id, updatedTask);
        return updatedTask;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTask(final Long id) throws Exception {
        final Task task = tasks.get(id);
        if (task == null) {
            throw new Exception(CAN_NOT_FIND_TASK_EXCEPTION);
        }
        return task;
    }
}
