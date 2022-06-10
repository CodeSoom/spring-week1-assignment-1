package com.codesoom.assignment.todo.services;

import com.codesoom.assignment.todo.models.Task;
import com.codesoom.assignment.todo.models.TaskIndex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class TaskService {
  private static final int LENGTH_OF_PATH_WITH_VALUE = 3;
  private static final int LOCATION_OF_VALUE_IN_PATH = 2;
  private static final long DEFAULT_VALUE_ASSIGNED_TO_LONG_TYPE = 0L;
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final HashMap<Long, Task> taskMap = new HashMap<>();
  private static TaskIndex taskIndex = new TaskIndex();

  public Long getTaskIdFromPath(String sRequestPath) {
    String[] arrPath = sRequestPath.split("/");

    // Specify a value if task id exists in the path, and initializes to 0 if not.
    // 경로에 taskId 값이 존재한다면 해당 값으로, 존재하지 않는다면 long 타입의 기본값인 0L으로 선언한다.
    try {
      return arrPath.length == LENGTH_OF_PATH_WITH_VALUE
          ? Long.parseLong(arrPath[LOCATION_OF_VALUE_IN_PATH])
          : DEFAULT_VALUE_ASSIGNED_TO_LONG_TYPE;
    } catch (NumberFormatException ne) {
      throw ne;
    }
  }

  public String getTasks(long lTaskId) throws IOException {
    if (lTaskId == 0L) {
      return tasksToJson();
    } else {
      return tasksToJson(taskMap.get(lTaskId));
    }
  }

  public String addTask(Task task) throws Exception {

    Long lTaskId = taskIndex.getNextIndex();

    if (isAlreadyExistTask(lTaskId)) {
      throw new Exception("ALREADY_EXIST");
    }

    task.setId(lTaskId);
    taskMap.put(lTaskId, task);

    taskIndex.addLastIndex();

    return tasksToJson(task);
  }

  public String modTask(Long lTaskId, Task task) throws Exception {
    if (!isAlreadyExistTask(lTaskId)) {
      throw new Exception("NOT_EXIST");
    } else {
      taskMap.get(lTaskId).setTitle(task.getTitle());
      return tasksToJson(taskMap.get(lTaskId));
    }
  }

  public String delTask(Long lTaskId) throws Exception {
    if (!isAlreadyExistTask(lTaskId)) {
      throw new Exception("NOT_EXIST");
    }
    taskMap.remove(lTaskId);
    return tasksToJson();
  }

  public Task toTask(String sRequestBody) throws JsonProcessingException {
    return objectMapper.readValue(sRequestBody, Task.class);
  }

  public String tasksToJson(Task task) throws IOException {

    OutputStream outputStream = new ByteArrayOutputStream();

    objectMapper.writeValue(outputStream, task);

    return outputStream.toString();
  }

  public String tasksToJson() throws IOException {

    OutputStream outputStream = new ByteArrayOutputStream();

    objectMapper.writeValue(outputStream, taskMap.values());

    return outputStream.toString();
  }

  public boolean isAlreadyExistTask(Long lTaskId) {
    return taskMap.containsKey(lTaskId);
  }
}
