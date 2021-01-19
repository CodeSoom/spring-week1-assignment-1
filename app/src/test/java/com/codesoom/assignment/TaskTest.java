package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    Task task;
    List<Task> tasks;

    @BeforeEach
    void init() {
        tasks = Arrays.asList(
                new Task("title1"),
                new Task("title2"),
                new Task("title3")
        );
    }

    @Disabled
    @Test
    void testGet(){
        assertEquals(task.getId(), 1L);
    }

    @Disabled
    @Test
    void testUpdateTitle(){
        task.updateTitle("newTitle");
        assertEquals(task.getTitle(), "newTitle");
    }

    @Test
    void test_id값_자동증가(){
        assertEquals(2, tasks.get(1).getId());
    }


}
