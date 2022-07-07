package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    final TaskService taskService = new TaskService();

    @Test
    @DisplayName("Task를 생성하고 Task를 반환하는지 테스트한다.")
    void createTaskTest() {
        Task taskToEquals = new Task(0L, "BJP");
        assertEquals(taskToEquals, taskService.createTask("BJP"));
    }

    @Test
    @DisplayName("가진 tasks를 반환하는지 테스트한다.")
    void getTasksTest() {
        assertEquals(0, taskService.getTasks().size());
    }
}