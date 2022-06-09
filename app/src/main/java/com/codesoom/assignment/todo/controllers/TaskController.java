package com.codesoom.assignment.todo.controllers;

import com.codesoom.assignment.todo.models.Task;
import com.codesoom.assignment.todo.services.TaskService;

import java.io.IOException;

public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  public String getTasks(String sRequestPath) throws IOException {
    Long lTaskId = taskService.getTaskIdFromPath(sRequestPath);
    return taskService.getTasks(lTaskId);
  }

  public String addTask(String sRequestBody) throws Exception {
    Task task = taskService.toTask(sRequestBody);
    return taskService.addTask(task);
  }

  public String modTask(String sRequestPath, String sRequestBody) throws Exception {
    Long lTaskId = taskService.getTaskIdFromPath(sRequestPath);
    Task task = taskService.toTask(sRequestBody);
    return taskService.modTask(lTaskId, task);
  }

  public String delTask(String sRequestPath) throws Exception {
    Long lTaskId = taskService.getTaskIdFromPath(sRequestPath);
    return taskService.delTask(lTaskId);
  }
}
