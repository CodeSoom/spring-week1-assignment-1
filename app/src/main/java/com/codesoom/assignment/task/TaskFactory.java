package com.codesoom.assignment.task;

import com.codesoom.assignment.task.repository.MemoryTaskRepository;
import com.codesoom.assignment.task.repository.TaskRepository;

public class TaskFactory {

  private TaskFactory() {
  }

  public static TaskRepository taskRepository() {
    return new MemoryTaskRepository();
  }

  public static TaskHandler taskHandler() {
    return new TaskHandler(taskRepository());
  }
}
