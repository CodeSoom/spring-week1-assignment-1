package com.codesoom.assignment.todo.controllers;

import com.codesoom.assignment.todo.models.Response;
import com.codesoom.assignment.todo.models.Task;
import com.codesoom.assignment.todo.services.TaskService;

public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  public Response getTasks(String sRequestPath) {

    try {
      Long lTaskId = taskService.getTaskIdFromPath(sRequestPath);
      String responseContent = taskService.getTasks(lTaskId);
      int responseStatus = "[]".equals(responseContent) ? Response.NO_CONTENT : Response.OK;
      return new Response(responseStatus, responseContent);
    } catch (Exception e) {
      return new Response(Response.BAD_REQUEST, "Task Id 는 숫자 포맷이어야 합니다");
    }
  }

  public Response addTask(String sRequestBody) {
    try {
      Task task = taskService.toTask(sRequestBody);
      String responseContent = taskService.addTask(task);
      return new Response(Response.CREATED, responseContent);
    } catch (Exception e) {
      return new Response(Response.CONFLICT, "알 수 없는 이유로 생성하지 못했습니다");
    }
  }

  public Response modTask(String sRequestPath, String sRequestBody) {
    try {
      Long lTaskId = taskService.getTaskIdFromPath(sRequestPath);
      Task task = taskService.toTask(sRequestBody);
      String responseContent = taskService.modifyTask(lTaskId, task);
      return new Response(Response.OK, responseContent);
    } catch (Exception e) {
      if (e.getMessage().equals("NOT_EXIST")) {
        return new Response(Response.NOT_FOUND, "Task Id를 찾을 수 없어, 수정하지 못했습니다");
      }
      return new Response(Response.BAD_REQUEST, "Task Id 는 숫자 포맷이어야 합니다");
    }
  }

  public Response delTask(String sRequestPath) {
    try {
      Long lTaskId = taskService.getTaskIdFromPath(sRequestPath);
      String responseContent = taskService.deleteTask(lTaskId);
      return new Response(Response.OK, responseContent);
    } catch (Exception e) {
      if (e.getMessage().equals("NOT_EXIST")) {
        return new Response(Response.NOT_FOUND, "Task Id를 찾을 수 없어, 삭제하지 못했습니다");
      }
      return new Response(Response.BAD_REQUEST, "Task Id 는 숫자 포맷이어야 합니다");
    }
  }
}
