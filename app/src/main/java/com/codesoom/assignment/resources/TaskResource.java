package com.codesoom.assignment.resources;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.InvalidRequestStateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TaskResource implements ResourceHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    protected static final List<Task> tasks = new ArrayList<>();

    protected Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content, Task.class);
    }

    protected String tasksToJSON() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    protected String taskToJSON(Task task) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    protected Long parseParam(String param) {
        if (param.isBlank()) {
            throw new InvalidRequestStateException("No Id passed");
        }
        return Long.parseLong(param);
    }

    protected Optional<Task> getTaskById(Long id) {
        return tasks.stream()
                .filter(i -> i.getId() == id)
                .reduce((i, v) -> {
                    throw new IllegalStateException("More than one ID exist");
                });
    }
}
