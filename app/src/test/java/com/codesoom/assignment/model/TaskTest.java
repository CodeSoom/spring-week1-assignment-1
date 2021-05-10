package com.codesoom.assignment.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskTest {
    @Test
    @DisplayName("Task를 갱신할 경우 id는 변경되지 않고 title만 변경됩니다")
    void testUpdatingTask() {
        var task = new Task(null, "test");
        var updatedTask = new Task(1L, "codesoom");

        task.update(updatedTask);

        assertNull(task.getId());
        assertEquals("codesoom", task.getTitle());
    }
}
