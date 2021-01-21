package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Service {
    Long id = 1L;
    ObjectMapper objectMapper;
    OutputStream outputStream;

    public Task getTask(Long idValue, List<Task> tasks) {
        Task getTask = null;
        for(Task task : tasks) {
            if(task.getId() == idValue){
                getTask = task;
                break;
            }
        }
        return getTask;
    }

    public void createTask(Task postTask, List<Task> tasks) {
        postTask.setId(id++);
        tasks.add(postTask);
    }

    public void updateTask(Task updateTask, String title) {
        updateTask.setTitle(title);
    }

    public void deleteTask(Task deleteTask, List<Task> tasks) {
        tasks.remove(deleteTask);
    }

    public void writeContentWithOutputStream(HttpExchange exchange, String content) throws IOException {
        outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public Task jsonToTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    public String tasksToJson(List<Task> tasks) throws IOException {
        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    public String taskToJson(Task task) throws IOException {
        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }
}
