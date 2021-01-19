package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.NotFoundTask;
import com.codesoom.assignment.domain.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskApplicationService {
    Map<Long, Task> taskMap = new HashMap<>();

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public Long createTask(String title) {
        Long id = getNextId();
        Task newTask = new Task(id, title);
        taskMap.put(id, newTask);
        return id;
    }

    public Task findTask(Long taskId) throws NotFoundTask {
        if (!taskMap.containsKey(taskId)) {
            throw new NotFoundTask();
        }
        return taskMap.get(taskId);
    }

    private Long getNextId() {
        return (long) taskMap.size();
    }

    public void updateTaskTitle(Long taskId, String newTitle) throws NotFoundTask {
        taskMap.put(taskId, findTask(taskId).updateTaskTitle(newTitle));
    }

    public void deleteTask(Long taskId) throws NotFoundTask {
        Task task = findTask(taskId);
        taskMap.remove(task.getId());
    }
}
