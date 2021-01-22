package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.JsonParser;

import java.util.List;

public class TaskService {

    private JsonParser jsonParser = new JsonParser();
    private TaskRepository taskRepository = TaskRepository.getInstance();

    public String getTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.size() == 0) {
            return "[]";
        }

        String content = jsonParser.toJSON(tasks);

        return content;
    }

    public String getTask(Long id) {
        Task task = taskRepository.findOne(id);
        if (task == null) {
            return "";
        }

        String content = jsonParser.toJSON(task);

        return content;
    }

    public String addTask(String body) {
        Task task = taskRepository.save(jsonParser.toTask(body));
        String content = jsonParser.toJSON(task);

        return content;
    }

    public String updateTask(Long id, String body) {
        Task task = taskRepository.update(id, jsonParser.toTask(body));
        String content = jsonParser.toJSON(task);

        return content;
    }

    public void deleteTask(Long id) {
        taskRepository.delete(id);
    }

}
