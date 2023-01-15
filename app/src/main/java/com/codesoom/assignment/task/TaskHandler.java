package com.codesoom.assignment.task;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.repository.TaskRepository;
import com.codesoom.assignment.task.utils.Converter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {

  public static final String GET_METHOD_STRING = "GET";
  public static final String POST_METHOD_STRING = "POST";
  public static final String TASKS_PATH_STRING = "/tasks";
  private final Converter converter = new Converter();
  private final TaskRepository taskRepository;

  public TaskHandler(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    String path = httpExchange.getRequestURI()
        .getPath();

    // dispatcher
    if (GET_METHOD_STRING.equals(method) && TASKS_PATH_STRING.equals(path)) {
      handleGetAllTasks(httpExchange);
      return;
    }
    if (POST_METHOD_STRING.equals(method) && TASKS_PATH_STRING.equals(path)) {
      handleCreateTask(httpExchange);
      return;
    }
    handleNotFound(httpExchange);
  }

  private void handleGetAllTasks(HttpExchange httpExchange) throws IOException {
    String content = converter.toJson(taskRepository.findAll());
    sendResponse(httpExchange, content, HTTP_OK);
  }

  private void handleCreateTask(HttpExchange httpExchange) throws IOException {
    String bodyString = getBodyString(httpExchange);
    Task task = converter.toTask(bodyString);
    taskRepository.save(task);
    sendResponse(httpExchange, converter.toJson(task), HTTP_CREATED);
  }

  private void handleNotFound(HttpExchange httpExchange) throws IOException {
    sendResponse(httpExchange, "", HTTP_NOT_FOUND);
  }

  private static void sendResponse(HttpExchange httpExchange, String content, int statusCode)
      throws IOException {
    httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);
    OutputStream outputStream = httpExchange.getResponseBody();
    outputStream.write(content.getBytes());
    outputStream.flush();
    outputStream.close();
  }

  private static String getBodyString(HttpExchange httpExchange) {
    InputStream inputStream = httpExchange.getRequestBody();
    return new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining());
  }
}
