package com.codesoom.assignment.task.utils;

import com.codesoom.assignment.task.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Converter {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private Converter() {
  }

  public static Task toTask(String content) throws JsonProcessingException {
    return objectMapper.readValue(content, Task.class);
  }

  public static String toJson(Task task) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    objectMapper.writeValue(outputStream, task);
    return outputStream.toString();
  }

  public static String toJson(List<Task> tasks) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    objectMapper.writeValue(outputStream, tasks);
    return outputStream.toString();
  }
}
