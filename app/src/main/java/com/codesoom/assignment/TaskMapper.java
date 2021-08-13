package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJsonWith(Task task) throws IOException {
        return write(task);
    }

    public String toJson(List<Task> tasks) throws IOException {
        return write(tasks);
    }

    private String write(Object tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
