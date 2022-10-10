package com.codesoom.assignment.http;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 요청 바디 데이터를 Task 객체로 변환합니다.
     * @param requestBody 요청 바디
     * @return Task 객체
     * @throws JsonProcessingException
     */
    public static Task toTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    /**
     * Task 객체를 JSON으로 변환하여 반환합니다.
     * @param task
     * @return
     * @throws IOException
     */
    public static String toJSON(Task task) throws IOException {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            objectMapper.writeValue(outputStream, task);
            return outputStream.toString();
        }
    }

    /**
     * Task 목록을 JSON으로 변환하여 반환합니다.
     * @param tasks
     * @return
     * @throws IOException
     */
    public static String toJSON(List<Task> tasks) throws IOException {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            objectMapper.writeValue(outputStream, tasks);
            return outputStream.toString();
        }
    }
}
