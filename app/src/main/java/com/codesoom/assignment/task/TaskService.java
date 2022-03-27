package com.codesoom.assignment.task;

import com.codesoom.assignment.task.dto.TaskDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TaskService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskList taskList;

    public TaskService(final TaskList taskList) {
        this.taskList = taskList;
    }

    public String saveTask(final String requestBody) throws JsonProcessingException {

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);

        Task task = taskDto.toTask();
        Task savedTask = taskList.save(task);

        return objectMapper.writeValueAsString(savedTask);
    }

    public String getTasks() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, taskList.getTasks());

        return outputStream.toString();
    }

    public String getTask(final Long taskId) throws JsonProcessingException {

        Task task = taskList.findTaskById(taskId);

        return objectMapper.writeValueAsString(task);
    }

    public String replaceTask(final Long taskId, final String requestBody) throws JsonProcessingException {

        Task task = taskList.findTaskById(taskId);

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);
        task.setTitle(taskDto.getTitle());

        return objectMapper.writeValueAsString(task);
    }

    public String modifyTask(final Long taskId, final String requestBody) throws JsonProcessingException {

        Task task = taskList.findTaskById(taskId);

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);
        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }

        return objectMapper.writeValueAsString(task);
    }

    public void deleteTask(final Long taskId) {
        Task task = taskList.findTaskById(taskId);

        taskList.remove(task);
    }
}
