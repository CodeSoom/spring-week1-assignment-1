package com.codesoom.assignment.task.utils;

import com.codesoom.assignment.task.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Converter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public Task toTask(String content) throws JsonProcessingException {
    return objectMapper.readValue(content, Task.class);
  }

  public String toJson(Task task) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    objectMapper.writeValue(outputStream, task);
    return outputStream.toString();
  }

  public String toJson(List<Task> tasks) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    objectMapper.writeValue(outputStream, tasks);
    return outputStream.toString();
  }
}
