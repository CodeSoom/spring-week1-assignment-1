package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Title;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TodoHttpHandlerUtilsTest {

    private Map<Long, Task> tasks = new HashMap<>();

    @BeforeEach
    public void cleanTasks() {
        tasks.clear();;
    }

    @Test
    @DisplayName("toTitle 메서드는 Http body에 온 content를 Title로 반환한다")
    void toTitle() throws JsonProcessingException {
        // given
        String content = "{\"title\" : \"과제하기\"}";

        // when
        Title title = TodoHttpHandlerUtils.toTitle(content);

        // then
        Assertions.assertEquals("과제하기", title.getTitle());
    }

    @Test
    @DisplayName("getId 메서드는 URL에서 id 파라미터를 추출한다")
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
    @DisplayName("taskToJSON 메서드는 Task 객체를 JSON으로 변환한다")
    void taskToJSON() throws IOException {
        // given
        Task task = new Task();
        task.setTitle("과제하기");

        // when
        String jsonString = TodoHttpHandlerUtils.taskToJSON(task);

        //Then
        Assertions.assertEquals("{\"id\":1,\"title\":\"과제하기\"}", jsonString);
    }

    @Test
    @DisplayName("toTask 메서드는 문자열을 Task 객체로 변환한다")
    void toTask() throws JsonProcessingException {
        // given
        String content = "{\"title\" : \"과제하기\"}";

        // when
        Task task = TodoHttpHandlerUtils.toTask(content);

        // then
        Assertions.assertTrue("과제하기".equals(task.getTitle()));
    }

    @Test
    @DisplayName("tasksToJSON 메서드는 task들을 JSON으로 변환한다")
    void tasksToJSON() throws IOException {
        // given
        Task task1 = new Task();
        task1.setTitle("과제하기1");

        Task task2 = new Task();
        task2.setTitle("과제하기2");

        tasks.put(task1.getId(), task1);
        tasks.put(task2.getId(), task2);

        // when
        String jsonString = TodoHttpHandlerUtils.tasksToJSON(tasks);
        System.out.println(jsonString);
        // then
        Assertions.assertTrue("[{\"id\":0,\"title\":\"과제하기1\"},{\"id\":1,\"title\":\"과제하기2\"}]".equals(jsonString));
    }
}