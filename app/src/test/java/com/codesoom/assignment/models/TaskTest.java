package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @Test
    void makeTask() {
        long id = 1;
        String title = "sample";
        Task task = new Task(id, title);

        assertEquals(id, task.id());
        assertEquals(title, task.title());
    }
}
