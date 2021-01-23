package com.codesoom.assignment.helper;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Change task or task list to JSON style text.
 * TODO: Merge methods using Generic.
 */
public class JSONParser {
    ObjectMapper objectMapper = new ObjectMapper();

    public String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    public String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
