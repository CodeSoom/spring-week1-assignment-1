package com.codesoom.assignment.task;

import static com.codesoom.assignment.task.utils.Converter.toJson;
import static com.codesoom.assignment.task.utils.Converter.toTask;
import static com.codesoom.assignment.task.utils.HttpClient.sendResponse;
import static com.codesoom.assignment.task.utils.Parser.extractId;
import static com.codesoom.assignment.task.utils.Parser.parseRequestBody;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.repository.TaskRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Optional;

public class TaskHandler implements HttpHandler {

  public static final String GET_METHOD = "GET";
  public static final String POST_METHOD = "POST";
  public static final String PUT_METHOD = "PUT";
  public static final String PATCH_METHOD = "PATCH";
  public static final String DELETE_METHOD = "DELETE";
  public static final String TASKS_PATH = "/tasks";
  private final TaskRepository taskRepository;

  public TaskHandler(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    String path = httpExchange.getRequestURI().getPath();

    if (path.isBlank() || !path.startsWith(TASKS_PATH)) {
      handleNotFound(httpExchange);
      return;
    }

    if (GET_METHOD.equals(method) && TASKS_PATH.equals(path)) {
      handleGetAllTasks(httpExchange);
      return;
    }
    if (GET_METHOD.equals(method) && path.length() > TASKS_PATH.length() + 1) {
      handleGetTaskById(httpExchange, path);
      return;
    }
    if (POST_METHOD.equals(method) && TASKS_PATH.equals(path)) {
      handleCreateTask(httpExchange);
      return;
    }
    if ((PUT_METHOD.equals(method) || PATCH_METHOD.equals(method))
        && path.length() > TASKS_PATH.length() + 1) {
      handleUpdateTask(httpExchange, path);
      return;
    }
    if (DELETE_METHOD.equals(method) && path.startsWith(TASKS_PATH)) {
      handleDeleteTask(httpExchange, path);
      return;
    }
    handleNotFound(httpExchange);
  }

  private void handleUpdateTask(HttpExchange httpExchange, String path) throws IOException {
    Long id = extractId(path, TASKS_PATH);
    String body = parseRequestBody(httpExchange);
    String title = toTask(body).getTitle();
    try {
      Task task = taskRepository.updateTitle(id, title);
      sendResponse(httpExchange, toJson(task), HTTP_OK);
    } catch (IllegalArgumentException e) {
      handleNotFound(httpExchange);
    }
  }

  private void handleDeleteTask(HttpExchange httpExchange, String path) throws IOException {
    Long id = extractId(path, TASKS_PATH);
    try {
      taskRepository.deleteById(id);
      sendResponse(httpExchange, "", HTTP_NO_CONTENT);
    } catch (IllegalArgumentException e) {
      handleNotFound(httpExchange);
    }
  }

  private void handleGetTaskById(HttpExchange httpExchange, String path) throws IOException {
    Long id = extractId(path, TASKS_PATH);
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (optionalTask.isPresent()) {
      sendResponse(httpExchange, toJson(optionalTask.get()), HTTP_OK);
      return;
    }
    handleNotFound(httpExchange);
  }

  private void handleGetAllTasks(HttpExchange httpExchange) throws IOException {
    String content = toJson(taskRepository.findAll());
    sendResponse(httpExchange, content, HTTP_OK);
  }

  private void handleCreateTask(HttpExchange httpExchange) throws IOException {
    String body = parseRequestBody(httpExchange);
    Task task = toTask(body);
    taskRepository.save(task);
    sendResponse(httpExchange, toJson(task), HTTP_CREATED);
  }

  private void handleNotFound(HttpExchange httpExchange) throws IOException {
    sendResponse(httpExchange, "", HTTP_NOT_FOUND);
  }
}
