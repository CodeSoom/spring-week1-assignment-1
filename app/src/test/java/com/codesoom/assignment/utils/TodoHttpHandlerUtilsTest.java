package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Title;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TodoHttpHandlerUtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Http body에 온 content를 Title로 잘 변환하는지")
    void toTitle() throws JsonProcessingException {
        // given
        String content = "{\"title\" : \"과제하기\"}";

        // when
        Title title = TodoHttpHandlerUtils.toTitle(content);

        // then
        Assertions.assertEquals("과제하기", title.getTitle());
    }

    @Test
    @DisplayName("URL에서 id 파라미터를 잘 추출하는지")
    void getId() {
        // given
        Long id = 100L;
        String path = "localhost:8000/tasks/100";

        // when
        Long resultId = TodoHttpHandlerUtils.getId(path);

        // then
        Assertions.assertEquals(id, resultId);
    }

    @Test
    @DisplayName("Task 객체를 JSON으로 잘 변환하는지")
    void taskToJSON() throws IOException {
        // given
        Task task = new Task();
        task.setTitle("과제하기");

        // when
        String jsonString = TodoHttpHandlerUtils.taskToJSON(task);
        System.out.println(jsonString);

        //Then
        Assertions.assertEquals("{\"id\":1,\"title\":\"과제하기\"}", jsonString);
    }

    @Test
    void toTask() {
    }

    @Test
    void tasksToJSON() {
    }

    @Test
    void writeOutputStream() {
    }
}