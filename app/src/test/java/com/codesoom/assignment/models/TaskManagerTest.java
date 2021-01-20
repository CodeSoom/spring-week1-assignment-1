package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskManagerTest {
    @Test
    void findIfNotExist() {
        Task task = TaskManager.find(100);
        assertNull(task);
    }

    @Test
    void successInsertTitle() {
        Task task = TaskManager.insert("sample");
        assertEquals("sample", task.title());
        assertEquals(1, task.id());
    }

    @Test
    void successInsertTask() throws Exception {
        TaskManager.insert(new Task(2, "sample2"));
        Task task = TaskManager.find(2);
        assertEquals("sample2", task.title());
        assertEquals(2, task.id());
    }

    @Test
    void failInsert() {
        TaskManager.insert("sample");

        try {
            TaskManager.insert(new Task(1, "sample"));
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("id 1 is already exists", e.getMessage());
        }
    }

    @Test
    void successModify() throws Exception {
        TaskManager.insert("sample");
        TaskManager.modify(new Task(1, "sample1"));

        Task task = TaskManager.find(1);
        assertEquals("sample1", task.title());
    }

    @Test
    void failModify() {
        try {
            TaskManager.modify(new Task(100, "not exist"));
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("not exist task id", e.getMessage());
        }
    }

    @Test
    void successDelete() throws Exception {
        TaskManager.insert("sample");
        TaskManager.delete(1);
        Task task = TaskManager.find(1);
        assertNull(task);
    }

    @Test
    void failDelete() {
        try {
            TaskManager.delete(100);
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("not exist task id", e.getMessage());
        }
    }
}
