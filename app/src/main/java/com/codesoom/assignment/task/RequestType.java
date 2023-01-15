package com.codesoom.assignment.task;

import static com.codesoom.assignment.task.TaskHandler.TASKS_PATH;

import java.util.Objects;

public enum RequestType {
  GET_TASKS, GET_TASK_BY_ID, POST_TASK, PUT_PATCH_TASK, DELETE_TASK, NOT_FOUND;

  private static final String GET_METHOD = "GET";
  private static final String POST_METHOD = "POST";
  private static final String PUT_METHOD = "PUT";
  private static final String PATCH_METHOD = "PATCH";
  private static final String DELETE_METHOD = "DELETE";

  public static RequestType of(String method, String path) {
    if (Objects.isNull(path) || path.isBlank() || !path.startsWith(TASKS_PATH)) {
      return RequestType.NOT_FOUND;
    }
    if (GET_METHOD.equals(method) && TASKS_PATH.equals(path)) {
      return RequestType.GET_TASKS;
    }
    if (GET_METHOD.equals(method) && path.length() > TASKS_PATH.length() + 1) {
      return RequestType.GET_TASK_BY_ID;
    }
    if (POST_METHOD.equals(method) && TASKS_PATH.equals(path)) {
      return RequestType.POST_TASK;
    }
    if ((PUT_METHOD.equals(method) || PATCH_METHOD.equals(method))
        && path.length() > TASKS_PATH.length() + 1) {
      return RequestType.PUT_PATCH_TASK;
    }
    if (DELETE_METHOD.equals(method) && path.startsWith(TASKS_PATH)) {
      return RequestType.DELETE_TASK;
    }
    return RequestType.NOT_FOUND;
  }
}
