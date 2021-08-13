package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

public class TaskFactory {
    private static Long sequence = 0L;
    private final JsonConverter jsonConverter = new JsonConverter();
    private final List<Map<String, Task>> tasks = new ArrayList<>();
    private final Map<String, Task> taskMap = new HashMap<>();

    public void createTask(String body) throws JsonProcessingException {
        Task task = jsonConverter.jsonToTask(body);
        task.setId(++sequence);
        taskMap.put(task.getId() + "", task);
        if(tasks.isEmpty()){
            tasks.add(taskMap);
        }
    }

    public void deleteTask(String id) {
        tasks.remove(id);
    }

    public Task updateTask(Task task, String content) throws JsonProcessingException {
        Task originTask = jsonConverter.jsonToTask(content);
        task.setTitle(originTask.getTitle());
        return task;
    }

    public List<Map<String, Task>> getTasks() {
        return tasks;
    }

    public Optional findId(String id) {
        Optional<Task> task = Optional.empty();
        Task findTask = taskMap.get(id);
        if (findTask == null) {
            return task;
        }
        return task.of(findTask);
    }
}
