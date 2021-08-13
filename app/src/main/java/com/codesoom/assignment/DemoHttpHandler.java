//TODO arraylist에서 찾는메서드 찾아보기
//TODO 변수이름 타입 지우기
//Todo :반환결과 고민하기
//TODO: 다양한 url 소화하기

package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final List<Task> tasks = new ArrayList<>();
  int createdStatusCode = 201;
  int okStatusCode = 200;
  int badRequestStatusCode = 400;
  int notFoundStatusCode = 404;
  int noContentStatusCode = 204;
  Long id = 0L;

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String content = "Hello codesoom";

    String method = exchange.getRequestMethod();
    URI uri = exchange.getRequestURI();
    String path = uri.getPath();

    int taskId = Integer.parseInt(getId(path));
    System.out.println(taskId);

    InputStream inputStream = exchange.getRequestBody();
    String body = new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining("\n"));

    System.out.println(method + " " + path);

    if (!body.isBlank() && method.equals("POST")) {

      Task task = toTask(body);
      task.setId(++id);
      tasks.add(task);
    }

    //GET /tasks
    if (method.equals("GET") && path.equals("/tasks")) {
      content = tasksToJson();
      exchange.sendResponseHeaders(okStatusCode, content.getBytes().length);

    }

    //GET tasks/{taskId}
    if (method.equals("GET") && path.equals("/tasks/" + taskId)) {
      Task targetTask = tasks.get(taskId - 1);
      content = targetTaskToJson(targetTask);

      exchange.sendResponseHeaders(okStatusCode, content.getBytes().length);

    }

    //POST tasks
    if (method.equals("POST") && path.equals("/tasks")) {
      content = "Create a new task";
      exchange.sendResponseHeaders(createdStatusCode, content.getBytes().length);

    }

    if (method.equals("PUT") && path.equals("/tasks/" + taskId)) {
      int indexFromTaskId;
      indexFromTaskId = taskId - 1;
      Task target_task = tasks.get(indexFromTaskId);
      Task change_task = toTask(body);
      target_task.setTitle(change_task.getTitle());
      content = "change target task";

      exchange.sendResponseHeaders(okStatusCode, content.getBytes().length);

    }

    if (method.equals("PATCH") && path.equals("/tasks/" + taskId)) {
      int indexFromTaskId;
      indexFromTaskId = taskId - 1;
      Task target_task = tasks.get(indexFromTaskId);
      Task change_task = toTask(body);
      target_task.setTitle(change_task.getTitle());
      content = "change target task";

      exchange.sendResponseHeaders(okStatusCode, content.getBytes().length);

    }

    if (method.equals("DELETE") && path.equals("/tasks/" + taskId)) {

      System.out.println("qweqweqw");
      String deleteStatus = deleteTask(taskId);
      if (deleteStatus == "Delete success") {
        content = "Delete success";
        exchange.sendResponseHeaders(noContentStatusCode, content.getBytes().length);

      } else {
        content = "fail";
        exchange.sendResponseHeaders(notFoundStatusCode, content.getBytes().length);
      }

    }

    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(content.getBytes());
    outputStream.flush();
    outputStream.close();

  }

  private String getId(String path) {
    return path.replace("/tasks/", "");
  }

  private Task toTask(String content) throws JsonProcessingException {
    return objectMapper.readValue(content, Task.class);
  }


  private String targetTaskToJson(Task task) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    OutputStream outputStream = new ByteArrayOutputStream();

    objectMapper.writeValue(outputStream, task);

    return outputStream.toString();
  }

  private String tasksToJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    OutputStream outputStream = new ByteArrayOutputStream();

    objectMapper.writeValue(outputStream, tasks);

    return outputStream.toString();
  }

  private String deleteTask(long ID) throws IOException {
    for (Task task : tasks) {
      if (task.getId() == ID) {
        tasks.remove(task);
        return "Delete success";
      }
    }
    return "fail";
  }


}
