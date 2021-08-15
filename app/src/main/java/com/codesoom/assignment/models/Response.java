package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class Response {
    private final String content;
    private final HttpStatus httpStatus;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String EMPTY_CONTENT = "";

    public Response(HttpStatus httpStatus) {
        content = EMPTY_CONTENT;
        this.httpStatus = httpStatus;
    }

    public Response(Task task, HttpStatus httpStatus) throws IOException {
        content = taskToJson(task);
        this.httpStatus = httpStatus;
    }

    public Response(Collection<Task> tasks, HttpStatus httpStatus) throws IOException {
        content = tasksToJson(tasks);
        this.httpStatus = httpStatus;
    }

    public byte[] getContentBytes() {
        return content.getBytes();
    }

    public Integer getStatusCode() {
        return httpStatus.getCode();
    }

    private String tasksToJson(Collection<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
