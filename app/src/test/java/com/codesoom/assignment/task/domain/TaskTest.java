package com.codesoom.assignment.task.domain;

import com.codesoom.assignment.task.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {
    @Test
    @DisplayName("Task Builder 생성 테스트")
    void createTask() {
        Task task = Task.builder()
                .id(1L)
                .title("test")
                .build();

        assertNotNull(task);
        System.out.println(task.toString());
    }

}