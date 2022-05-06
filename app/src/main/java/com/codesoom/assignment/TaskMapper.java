package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskMapper {
    ObjectMapper mapper;

    TaskMapper(){
        this.mapper = new ObjectMapper();
    }

    public Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content, Task.class);
    }

    public String tasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    public String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, task);

        return outputStream.toString();
    }


}
