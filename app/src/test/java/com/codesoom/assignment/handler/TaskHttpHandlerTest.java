package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskHttpHandlerTest {

    TaskHttpHandler taskHttpHandler = new TaskHttpHandler();

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Task 객체로 변경 테스트")
    void testToTask() throws JsonProcessingException {
        String content = "{\"id\":\"1\", \"title\":\"작업하기\"}";

        Task task = objectMapper.readValue(content, Task.class);
        assertNotNull(task);

        System.out.println(task);
    }
}