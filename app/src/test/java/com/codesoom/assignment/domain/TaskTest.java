package com.codesoom.assignment.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    @DisplayName("Task 의 ID 가 자동으로 잘 생성되는지 검증한다.")
    public void testTaskIdAutoGeneration() {
        Task task1 = new Task("Todo1");
        Task task2 = new Task("Todo2");
        Task task3 = new Task("Todo3");

        Assertions.assertEquals(task1.getTitle(), "Todo1");
        Assertions.assertEquals(task2.getTitle(), "Todo2");
        Assertions.assertEquals(task3.getTitle(), "Todo3");
        Assertions.assertEquals(task1.getId(), 1L);
        Assertions.assertEquals(task2.getId(), 2L);
        Assertions.assertEquals(task3.getId(), 3L);
    }
}