package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskHttpHandlerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void parseJsonToTask() throws JsonProcessingException {
        Task task = objectMapper.readValue("{ \"title\": \"과제 제출하기\" }", Task.class);
        Assertions.assertEquals(task.getId(), 1);
        Assertions.assertEquals(task.getTitle(), "과제 제출하기");
    }
}