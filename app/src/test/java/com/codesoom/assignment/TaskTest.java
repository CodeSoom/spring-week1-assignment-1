package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {


    @Test
    void getTest() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("homework");

        assertEquals(task.getId(), 1L);
        assertEquals(task.getTitle(), "homework");
    }
}
