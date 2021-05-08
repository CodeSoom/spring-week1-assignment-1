package com.codesoom.assignment.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskTest {
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testUnmarshallingJSONIntoTask() throws JsonProcessingException {
        var json = "{\"title\":\"test\"}";
        var unmarshalledTask = objectMapper.readValue(json, Task.class);

        assertNull(unmarshalledTask.getId());
        assertEquals("test", unmarshalledTask.getTitle());
    }

    @Test
    void testUnmarshallingEmptyJSONIntoTask() throws JsonProcessingException {
        var json = "{}";
        var unmarshalledTask = objectMapper.readValue(json, Task.class);

        assertNull(unmarshalledTask.getId());
        assertNull(unmarshalledTask.getTitle());
    }

    @Test
    void testUpdatingTask() {
        var task = new Task(null, "test");
        var updatedTask = new Task(1L, "codesoom");

        task.update(updatedTask);

        assertNull(task.getId());
        assertEquals("codesoom", task.getTitle());
    }
}
