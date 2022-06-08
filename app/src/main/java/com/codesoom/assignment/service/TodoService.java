package com.codesoom.assignment.service;

import com.codesoom.assignment.Exception.NotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.MemoryTaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class TodoService {
    private ObjectMapper objectMapper = new ObjectMapper();
    private MemoryTaskRepository taskRepository = new MemoryTaskRepository();


    public String getTasks() throws IOException {
        List<Task> tasks = taskRepository.findAll();
        return tasksToJSON(tasks);
    }

    public String postTask(String content) throws IOException {
        Task task = JSONtoTask(content);
        taskRepository.save(task);
        return taskToJSON(task);
    }

    public String putTask(Long taskId, String content) throws IOException, NotFoundException {
        Task newTask = JSONtoTask(content);
        Optional<Task> updatedTask = taskRepository.update(taskId, newTask.getTitle());
        if (!updatedTask.isPresent()) {
            throw new NotFoundException("존재하지 않는 task");
        }
        return taskToJSON(updatedTask.get());
    }

    public String getTask(Long taskId) throws IOException, NotFoundException {
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (!foundTask.isPresent()) {
            throw new NotFoundException("존재하지 않는 Task");
        }
        return taskToJSON(foundTask.get());
    }

    //
    public String deleteTask(Long taskId) throws NotFoundException {
        Optional<Task> deleteTask = taskRepository.findById(taskId);
        if (!deleteTask.isPresent()) {
            throw new NotFoundException("존재하지 않는 Task");
        }
        taskRepository.delete(deleteTask.get());
        return "";
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private Task JSONtoTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }
}
