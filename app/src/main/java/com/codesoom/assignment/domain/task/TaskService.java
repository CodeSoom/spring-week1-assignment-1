package com.codesoom.assignment.domain.task;

import java.util.List;

public interface TaskService {
    Task save(Task task);
    Task update(String taskId,String title);
    Task findById(String id);
    Task getById(String id);
    List<Task> allTasks();
}
