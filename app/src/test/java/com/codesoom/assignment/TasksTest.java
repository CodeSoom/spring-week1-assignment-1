package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TasksTest {
    private Tasks tasks;
    private Task task;

    @BeforeEach
    void setUp() {
        tasks = new Tasks();
        task = new Task(1L, "test");
    }

    @Test
    void addTask() {
        tasks.addTask(task);
        assertEquals(tasks.size(), 1);
    }

    @Test
    void remove() {
        tasks.addTask(task);
        tasks.remove(task);
        assertEquals(tasks.size(), 0);
    }

    @Test
    void findTask() {
        tasks.addTask(task);
        Optional<Task> task = tasks.findTask(1L);
        assertEquals(task.get().getId(), 1L);
    }

    @Test
    void getTasks() {
        tasks.addTask(task);
        assertNotNull(tasks.getTasks());
    }
}
