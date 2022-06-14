package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Response;
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


    public void getTasks(Response response) throws IOException {
        List<Task> tasks = taskRepository.findAll();
        response.setSuccess(tasksToJSON(tasks));
        return;
    }

    public void postTask(Response response, String content) throws IOException {
        Task task = JSONtoTask(content);
        taskRepository.save(task);
        response.setCreated( taskToJSON(task));
        return;
    }

    public void putTask(Response response, Long taskId, String content) throws IOException{
        Task newTask = JSONtoTask(content);
        Optional<Task> updatedTask = taskRepository.update(taskId, newTask.getTitle());
        if (!updatedTask.isPresent()) {
             response.setNotFound("Task를 찾지 못해 수정하지 못했습니다.");
             return;
        }
        response.setSuccess(taskToJSON(updatedTask.get()));
        return;
    }

    public void getTask(Response response, Long taskId) throws IOException{
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (!foundTask.isPresent()) {
            response.setNotFound("Task를 찾지 못했습니다.");
            return;
        }
        response.setSuccess(taskToJSON(foundTask.get()));
        return;
    }

    //
    public void deleteTask(Response response, Long taskId){
        Optional<Task> deleteTask = taskRepository.findById(taskId);
        if (!deleteTask.isPresent()) {
            response.setNotFound("Task를 찾지 못해 삭제하지 못했습니다.");
            return;
        }
        taskRepository.delete(deleteTask.get());
        response.setDeleteSuccess("");
        return ;
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
