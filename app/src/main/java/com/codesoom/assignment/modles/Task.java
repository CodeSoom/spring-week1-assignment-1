package com.codesoom.assignment.modles;

import com.codesoom.assignment.IdGenerator;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public final class Task {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final InjectableValues.Std VALUE_INJECTER = new InjectableValues.Std();

    private final Long id;
    private String title;

    public Task(@JacksonInject final Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Optional<Task> jsonToTask(final String content) {
        Task task = null;
        try {
            VALUE_INJECTER.addValue(Long.class, IdGenerator.generateId());
            OBJECT_MAPPER.setInjectableValues(VALUE_INJECTER);
            task = OBJECT_MAPPER.readValue(content, Task.class);
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
            IdGenerator.undoIdGeneration();
        }
        return Optional.ofNullable(task);
    }

    public static Task jsonToTaskOrNull(final String content) {
        try {
            VALUE_INJECTER.addValue(Long.class, IdGenerator.generateId());
            OBJECT_MAPPER.setInjectableValues(VALUE_INJECTER);
            return OBJECT_MAPPER.readValue(content, Task.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            IdGenerator.undoIdGeneration();
            return null;
        }
    }
}
