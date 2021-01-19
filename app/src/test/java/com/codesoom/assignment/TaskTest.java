package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    Task task;
    
    @BeforeEach
    private void init() {
        task = new Task(1L, "title");
    }

    @Test
    void testGet(){
        assertEquals(task.getId(), 1L);
    }

    @Test
    void testUpdateTitle(){
        task.updateTitle("newTitle");
        assertEquals(task.getTitle(), "newTitle");
    }





}
