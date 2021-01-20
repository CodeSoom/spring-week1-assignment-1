package com.codesoom.assignment.models;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    @BeforeEach
    void beforeEach() {
        TaskManager.clear();
    }

    @Test
    void findIfNotExist() {
        try {
            TaskManager.find(100);
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
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
        } catch (AlreadyExistsIDException ignored) {
            return;
        }
        fail();
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
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }

    @Test
    void successDelete() throws Exception {
        TaskManager.insert("sample");
        TaskManager.delete(1);

        try {
            TaskManager.find(1);
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }

    @Test
    void failDelete() {
        try {
            TaskManager.delete(100);
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }
}
