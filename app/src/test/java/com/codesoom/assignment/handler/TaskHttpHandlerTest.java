package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskHttpHandlerTest {

    TaskHttpHandler taskHttpHandler = new TaskHttpHandler();
    ObjectMapper objectMapper = new ObjectMapper();

    private static List<Task> tasks = new ArrayList<>();

    @BeforeAll
    public static void initData() {
        long index = 1L;
        tasks.add(Task.builder().id(index++).title("책읽기").build());
        tasks.add(Task.builder().id(index++).title("개발하기").build());
        tasks.add(Task.builder().id(index++).title("포스팅하기").build());
        tasks.add(Task.builder().id(index++).title("커밋하기").build());
        tasks.add(Task.builder().id(index++).title("게임하기").build());
        tasks.add(Task.builder().id(index++).title("운동하기").build());
        tasks.add(Task.builder().id(index++).title("술마시기").build());
        tasks.add(Task.builder().id(index++).title("잠자기").build());
    }

    @Test
    @DisplayName("Task 전체 조회")
    void testFindByTaskAll() {
        // 그대로 사용
        tasks.forEach(System.out::println);
    }

    @Test
    @DisplayName("Task 조회")
    void testFindByTaskId() {
        long testIndex = 3L;
        String testTitle = "포스팅하기";

        Task task = tasks.stream().filter(it -> it.getId() == testIndex).findFirst().orElse(null);

        assertEquals(task.getTitle(), testTitle);

        System.out.println(task.toString());
    }

    @Test
    @DisplayName("Task 저장")
    void testSaveTask() {
        long id = tasks.size()+1;
        String newTitle = "저장하기";
        Task task = Task.builder()
                .id(id)
                .title(newTitle)
                .build();

        tasks.add(task);
        Task newTask = tasks.stream().filter(it -> it.getId() == id).findFirst().orElse(null);

        assertEquals(newTask.getTitle(), newTitle);

        System.out.println(newTask);
    }

    @Test
    @DisplayName("Task 업데이트")
    void testUpdateTask() {
        long id = 1L;
        String updateTitle = "첫번째꺼 업데이트";
        Task task = tasks.stream().filter(it -> it.getId() == id).findFirst().get();
        task.setTitle(updateTitle);

        Task compareTask = tasks.stream().filter(it -> it.getId() == id).findFirst().get();
        assertEquals(compareTask.getTitle(), updateTitle);

        tasks.forEach(System.out::println);
    }

    @Test
    @DisplayName("Task 삭제")
    void testRemoveTask() {
        long id = 1L;
        Task task = tasks.stream().filter(it -> it.getId() == id).findFirst().get();

        tasks.remove(task);

        Optional<Task> removedTask = tasks.stream().filter(it -> it.getId() == id).findFirst();

        assertTrue(removedTask.isEmpty());
    }

    @Test
    @DisplayName("Task 객체로 변경 테스트")
    void testToTask() throws JsonProcessingException {
        String content = "{\"id\":\"1\", \"title\":\"작업하기\"}";

        Task task = objectMapper.readValue(content, Task.class);
        assertNotNull(task);

        System.out.println(task);
    }

    @Test
    @DisplayName("Json 으로 변경 테스트")
    void testToJson() throws IOException {
        Task task = Task.builder().id(10L).title("테스트하기").build();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        assertNotNull(outputStream);

        System.out.println(outputStream.toString());
    }
}