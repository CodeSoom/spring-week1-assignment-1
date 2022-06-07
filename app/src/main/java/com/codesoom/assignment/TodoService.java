package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoService {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long sequence = 0L;

    public String getTasks() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    public String postTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content,Task.class);
        task.setId(sequence++);
        tasks.add(task);
        System.out.println(tasks.size());
        return "success";
    }

    public String putTasks(Long taskId, String content) throws JsonProcessingException {
        Optional<Task> changeTask = tasks.stream().filter(task-> task.getId().equals(taskId)).findAny();
        if(!changeTask.isPresent()){
            return "존재하지 않는 Task";
        }
        Task newTask = objectMapper.readValue(content,Task.class);
        changeTask.get().setTitle(newTask.getTitle());
        return "success";
    }

    public String getTask(Long taskId) throws IOException {
        Optional<Task> foundTask = tasks.stream().filter(task-> task.getId().equals(taskId)).findAny();
        if(!foundTask.isPresent()){
            return "존재하지 않는 Task";
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, foundTask.get());

        return outputStream.toString();
    }
//
    public String deleteTask(Long taskId) {
        Optional<Task> deleteTask = tasks.stream().filter(task-> task.getId().equals(taskId)).findAny();
        if(!deleteTask.isPresent()){
            return "존재하지 않는 Task";
        }
        tasks.remove(deleteTask.get());
        return "success";
    }
}
