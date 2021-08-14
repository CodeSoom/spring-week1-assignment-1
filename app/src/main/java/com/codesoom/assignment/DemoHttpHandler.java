//TODO 테스트코드 통과하기
//TODO 구조 고민해보기

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
  static final int CREATED_STATUS_CODE = 201;
  static final int OK_STATUS_CODE = 200;
  static final int BAD_REQUEST_STATUS_CODE = 400;
  static final int NOT_FOUND_STATUS_CODE = 404;
  static final int NO_CONTENT_STATUS_CODE = 204;

  Long id = 0L;

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String content = "Hello codesoom";

    String method = exchange.getRequestMethod();
    URI uri = exchange.getRequestURI();
    String path = uri.getPath();

    System.out.println(getId(path));

    int taskId = 0;
    if(getId(path) != ""){
      taskId = Integer.parseInt(getId(path));
    }

    InputStream inputStream = exchange.getRequestBody();
    String body = new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining("\n"));

    System.out.println(method + " " + path);

    if (!body.isBlank() && method.equals("POST")) {

      Task task = toTask(body);
      id++;
      task.setId(id);
      tasks.add(task);
    }

    //GET /tasks
    if (method.equals("GET") && path.equals("/tasks")) {
      content = tasksToJson();
      exchange.sendResponseHeaders(OK_STATUS_CODE, content.getBytes().length);

    }

    //GET tasks/{taskId}
    if (method.equals("GET") && path.equals("/tasks/" + taskId)) {
      Task targetTask = null;

      for (Task task : tasks) {
        if (task.getId() == taskId) {
          targetTask = task;
          content = targetTaskToJson(targetTask);

        }
      }

      if (targetTask != null) {
        exchange.sendResponseHeaders(OK_STATUS_CODE, content.getBytes().length);
      }else{
        exchange.sendResponseHeaders(NOT_FOUND_STATUS_CODE, content.getBytes().length);

      }
    }

    //POST tasks
    if (method.equals("POST") && path.equals("/tasks")) {
      content = "Create a new task";
      exchange.sendResponseHeaders(CREATED_STATUS_CODE, content.getBytes().length);
    }


    if ((method.equals("PATCH")||method.equals("PUT") )&& path.equals("/tasks/" + taskId) ) {
      Boolean isTaskRewrite = rewriteTask(taskId, body);
      if (isTaskRewrite == true) {
        content = "target task changed";
        exchange.sendResponseHeaders(OK_STATUS_CODE, content.getBytes().length);
      } else {
        content = "fail";
        exchange.sendResponseHeaders(NOT_FOUND_STATUS_CODE, content.getBytes().length);
      }

    }

    if (method.equals("DELETE") && path.equals("/tasks/" + taskId)) {
      Boolean isTaskDeleted = deleteTask(taskId);
      if (isTaskDeleted == true) {
        content = "Delete success";
        exchange.sendResponseHeaders(NO_CONTENT_STATUS_CODE, content.getBytes().length);
      } else {
        content = "fail";
        exchange.sendResponseHeaders(NOT_FOUND_STATUS_CODE, content.getBytes().length);
      }
    }

    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(content.getBytes());
    outputStream.flush();
    outputStream.close();

  }

  private String getId(String path) {
    String[] splitBySlash= path.split("/");
    String id = "";
    if(splitBySlash.length > 2) {
      int placeOfId = 2;
      id = splitBySlash[placeOfId];
    }
    return id;
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

  private Boolean deleteTask(long ID) throws IOException {
    for (Task task : tasks) {
      if (task.getId() == ID) {
        tasks.remove(task);
        return true;
      }
    }
    return false;
  }
    private Boolean rewriteTask(long ID,String body ) throws IOException {
    for (Task task : tasks) {
      if (task.getId() == ID) {
        task.setTitle(body);
        return true;
      }
    }
    return false;
  }

}
