package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonParserTest {

    @Test
    void objToJson_paring_테스트() throws IOException {
        Task task = new Task(1L, "task1");
        String parsedTask = JsonParser.toJsonString(Lists.newArrayList(task));

        assertEquals(parsedTask, "{\"id\":1,\"title\":\"task1\"}");
    }


    @Test
    void jsonToObj_paring_테스트() throws JsonProcessingException {
        Task task = JsonParser.toTask("{\"id\":1,\"title\":\"task1\"}");

        assertEquals(task.getId(), 1L);
        assertEquals(task.getTitle(), "task1");
    }
}