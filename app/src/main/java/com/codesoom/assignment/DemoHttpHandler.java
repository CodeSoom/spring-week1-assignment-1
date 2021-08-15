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
  static final int INTERNAL_ERROR = 500;

  Long id = 0L;
  int STATUS_CODE = INTERNAL_ERROR;

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String content = "Hello codesoom";
    String method = exchange.getRequestMethod();
    URI uri = exchange.getRequestURI();
    String path = uri.getPath();
    System.out.println(uri);
    System.out.println(getId(path));

    Long taskId = null;
    if(getId(path) != ""){
      taskId = Long.parseLong(getId(path));
    }

    InputStream inputStream = exchange.getRequestBody();
    String body = new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining("\n"));

    System.out.println(method + " " + path);



    //GET /tasks
    if (method.equals("GET") && path.equals("/tasks")) {
      content = tasksToJson();
      STATUS_CODE = OK_STATUS_CODE;
    }

    //GET tasks/{taskId}
    if (method.equals("GET") && path.equals("/tasks/" + taskId)) {
      Task targetTask = findTask(taskId);
      content = targetTaskToJson(targetTask);
      if (targetTask != null) {
        STATUS_CODE = OK_STATUS_CODE;
      }else{
        STATUS_CODE = NOT_FOUND_STATUS_CODE;
      }
    }

    //POST tasks
    if (method.equals("POST") && path.equals("/tasks") && !body.isBlank()) {
      content = "Create a new task";
      postTask(body);
      STATUS_CODE = CREATED_STATUS_CODE;
    }


    if ((method.equals("PATCH")||method.equals("PUT") )&& path.equals("/tasks/" + taskId) ) {
      Boolean isTaskRewrite = rewriteTask(taskId, body);
      if (isTaskRewrite ) {
        content = "target task changed";
        STATUS_CODE = OK_STATUS_CODE;
      } else {
        content = "fail";
        STATUS_CODE = NOT_FOUND_STATUS_CODE;
      }

    }

    if (method.equals("DELETE") && path.equals("/tasks/" + taskId)) {
      Boolean isTaskDeleted = deleteTask(taskId);
      if (isTaskDeleted ) {
        content = "Delete success";
        STATUS_CODE = NO_CONTENT_STATUS_CODE;
      } else {
        content = "fail";
        STATUS_CODE = NOT_FOUND_STATUS_CODE;

      }
    }

    sendResponse(exchange, STATUS_CODE,content);

  }

  private void sendResponse(HttpExchange exchange,int STATUS_CODE, String content) throws IOException {
    exchange.sendResponseHeaders(STATUS_CODE, content.getBytes().length);
    OutputStream outputStream = exchange.getResponseBody();
    outputStream.write(content.getBytes());
    outputStream.flush();
    outputStream.close();
  }


  private void postTask(String body) throws JsonProcessingException {
    Task task = toTask(body);
    id++;
    task.setId(id);
    System.out.println(task);
    tasks.add(task);
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
  private Task findTask(Long id){
    return tasks.stream()
        .filter(task -> task.getId().equals(id))
        .findFirst().orElse(null);
  }

  private Boolean deleteTask(long id) throws IOException {
    Task task = findTask(id);
    if(task ==null){
      return false;
    }
      return tasks.remove(task);

    }

    private Boolean rewriteTask(long id,String body) throws IOException {
        Task task = findTask(id);
        if(task ==null){
        return false;
      }
      task.setTitle(body);
      return true;
        }

}
