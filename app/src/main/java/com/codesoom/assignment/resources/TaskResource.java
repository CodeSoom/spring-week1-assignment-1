package com.codesoom.assignment.resources;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
}
