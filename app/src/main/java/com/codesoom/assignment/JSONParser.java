package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    static String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(outputStream, task);
        return outputStream.toString();
    }

    static String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(outputStream, tasks);
        return  outputStream.toString();
    }
}
