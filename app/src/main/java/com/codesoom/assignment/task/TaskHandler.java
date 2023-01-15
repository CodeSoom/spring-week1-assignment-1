package com.codesoom.assignment.task;

import static com.codesoom.assignment.task.utils.Converter.toJson;
import static com.codesoom.assignment.task.utils.Converter.toTask;
import static com.codesoom.assignment.task.utils.HttpClient.sendResponse;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.repository.TaskRepository;
import com.codesoom.assignment.task.utils.Parser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Optional;

public class TaskHandler implements HttpHandler {

  public static final String TASKS_PATH = "/tasks";
  private final TaskRepository taskRepository;

  public TaskHandler(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    Parser parser = new Parser(httpExchange);
    String method = parser.getMethod();
    String path = parser.getPath();
    String body = parser.getBody();

    switch (RequestType.of(method, path)) {
      case GET_TASKS -> handleGetAllTasks(httpExchange);
      case GET_TASK_BY_ID -> handleGetTaskById(httpExchange, parser.getId(TASKS_PATH));
      case POST_TASK -> handleCreateTask(httpExchange, body);
      case PUT_PATCH_TASK -> handleUpdateTask(httpExchange, parser.getId(TASKS_PATH), body);
      case DELETE_TASK -> handleDeleteTask(httpExchange, parser.getId(TASKS_PATH));
      default -> handleNotFound(httpExchange);
    }
  }

  private void handleGetAllTasks(HttpExchange httpExchange) throws IOException {
    String content = toJson(taskRepository.findAll());
    sendResponse(httpExchange, content, HTTP_OK);
  }

  private void handleGetTaskById(HttpExchange httpExchange, long id) throws IOException {
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (optionalTask.isPresent()) {
      sendResponse(httpExchange, toJson(optionalTask.get()), HTTP_OK);
      return;
    }
    handleNotFound(httpExchange);
  }

  private void handleCreateTask(HttpExchange httpExchange, String body) throws IOException {
    Task task = taskRepository.save(toTask(body));
    sendResponse(httpExchange, toJson(task), HTTP_CREATED);
  }

  private void handleUpdateTask(HttpExchange httpExchange, long id, String body)
      throws IOException {
    String title = toTask(body).getTitle();
    try {
      Task task = taskRepository.updateTitle(id, title);
      sendResponse(httpExchange, toJson(task), HTTP_OK);
    } catch (IllegalArgumentException e) {
      handleNotFound(httpExchange);
    }
  }

  private void handleDeleteTask(HttpExchange httpExchange, long id) throws IOException {
    try {
      taskRepository.deleteById(id);
      sendResponse(httpExchange, "", HTTP_NO_CONTENT);
    } catch (IllegalArgumentException e) {
      handleNotFound(httpExchange);
    }
  }

  private void handleNotFound(HttpExchange httpExchange) throws IOException {
    sendResponse(httpExchange, "", HTTP_NOT_FOUND);
  }
}
