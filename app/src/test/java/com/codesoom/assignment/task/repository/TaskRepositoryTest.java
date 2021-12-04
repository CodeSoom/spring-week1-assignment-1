package com.codesoom.assignment.task.repository;

import com.codesoom.assignment.task.domain.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskRepositoryTest {

    private static TaskRepository taskRepository = new TaskRepository();

    @BeforeAll
    public static void initData() {
        long index = 1L;
        taskRepository.save(Task.builder().id(index++).title("책읽기").build());
        taskRepository.save(Task.builder().id(index++).title("개발하기").build());
        taskRepository.save(Task.builder().id(index++).title("포스팅하기").build());
        taskRepository.save(Task.builder().id(index++).title("커밋하기").build());
        taskRepository.save(Task.builder().id(index++).title("게임하기").build());
        taskRepository.save(Task.builder().id(index++).title("운동하기").build());
        taskRepository.save(Task.builder().id(index++).title("술마시기").build());
        taskRepository.save(Task.builder().id(index++).title("잠자기").build());
    }

    @Test
    @DisplayName("Task 전체 조회")
    void testFindByTaskAll() {
        // 그대로 사용
        List<Task> tasks = taskRepository.findAll();

        assertNotNull(tasks);

        tasks.forEach(System.out::println);
    }

    @Test
    @DisplayName("Task ID 필드 조회")
    void testFindByTaskId() {
        long testIndex = 3L;
        String testTitle = "포스팅하기";

        Task task = taskRepository.findById(testIndex);

        assertEquals(task.getTitle(), testTitle);

        System.out.println(task.toString());
    }

    @Test
    @DisplayName("새로운 Task 객체 생성 후 저장하기")
    void testSaveTask() {
        long newId = taskRepository.lastId() + 1;
        String newTitle = "저장하기";
        Task task = Task.builder()
                .id(newId)
                .title(newTitle)
                .build();

        Task newTask = taskRepository.save(task);

        assertEquals(newTask.getTitle(), newTitle);

        System.out.println(newTask);
    }

    @Test
    @DisplayName("첫번째 Task 객체 생성 후 업데이트")
    void testUpdateTask() {
        long updateId = 1L;
        String updateTitle = "첫번째꺼 업데이트";
        Task updateTask = Task.builder()
                .id(updateId)
                .title(updateTitle)
                .build();

        assertEquals(updateTask.getTitle(), updateTitle);

        taskRepository.findAll().forEach(System.out::println);
    }

    @Test
    @DisplayName("Task ID 조회 후 삭제")
    void testDeleteTask() {
        long removeId = 1L;
        Task task = taskRepository.findById(removeId);

        taskRepository.delete(task);

        Task checkTask = taskRepository.findById(removeId);

        assertNull(checkTask);
    }

}