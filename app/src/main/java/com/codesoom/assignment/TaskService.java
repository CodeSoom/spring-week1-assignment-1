package com.codesoom.assignment;

import com.codesoom.exception.TaskNotFoundException;

import java.io.IOException;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository = new TaskRepository();

    public String getTasks() throws IOException {
        List<Task> tasks = taskRepository.findAll();
        return JsonParser.objectToJson(tasks);
    }

    public String getTask(Long id) throws IOException {
        if (taskRepository.isExist(id)) {
            Task task = taskRepository.findById(id);
            return JsonParser.objectToJson(task);
        }
        throw new TaskNotFoundException();
    }

    public String createTask(String body) throws IOException {
        Task task = JsonParser.requestBodyToObject(body, Task.class);
        Task savedTask = taskRepository.save(task);

        return JsonParser.objectToJson(savedTask);
    }

    public String updateTask(Long id, String body) throws IOException {
        if (taskRepository.isExist(id)) {
            Task task = JsonParser.requestBodyToObject(body, Task.class);
            task.setId(id);

            Task updateTask = taskRepository.update(task);

            return JsonParser.objectToJson(updateTask);
        }
        throw new TaskNotFoundException();
    }

    public void deleteTask(Long id) {
        if (taskRepository.isExist(id)) {
            taskRepository.delete(id);
            return;
        }
        throw new TaskNotFoundException();
    }
}
