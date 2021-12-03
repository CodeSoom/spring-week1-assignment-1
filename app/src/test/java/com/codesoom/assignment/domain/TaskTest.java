package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {
    @Test
    @DisplayName("Task 생성 테스트")
    void createTask() {
        Task task = Task.builder()
                .id(1L)
                .title("test")
                .build();

        assertNotNull(task);
        System.out.println(task.toString());
    }

}