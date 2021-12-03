package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    @DisplayName("Task 생성 테스트")
    void createTask() {
        Task task = Task.builder()
                .id(1L)
                .title("test")
                .build();

        System.out.println(task.toString());
    }

}